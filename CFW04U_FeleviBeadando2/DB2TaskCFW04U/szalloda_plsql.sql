
CREATE TABLE ugyfelek (
    ugyfel_id NUMBER PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL,
    telefonszam VARCHAR2(20),
    husegpontok NUMBER DEFAULT 0,
    regisztracio_datuma DATE
);


CREATE TABLE foglalasok (
    foglalas_id NUMBER PRIMARY KEY,
    ugyfel_id NUMBER,
    szobaszam NUMBER,
    erkezes_datuma DATE,
    ar NUMBER,
    CONSTRAINT fk_ugyfel FOREIGN KEY (ugyfel_id) REFERENCES ugyfelek(ugyfel_id) ON DELETE CASCADE
);


CREATE TABLE ugyfel_naplo (
    log_id NUMBER PRIMARY KEY,
    muvelet_tipusa VARCHAR2(20),
    erintett_ugyfel_id NUMBER,
    muvelet_datuma TIMESTAMP
);

CREATE SEQUENCE seq_ugyfel_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_foglalas_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_log_id START WITH 1 INCREMENT BY 1;
/


CREATE OR REPLACE TRIGGER trg_ugyfel_auto_id
BEFORE INSERT ON ugyfelek
FOR EACH ROW
BEGIN
    IF :NEW.ugyfel_id IS NULL THEN
        :NEW.ugyfel_id := seq_ugyfel_id.NEXTVAL;
    END IF;
END;
/


CREATE OR REPLACE TRIGGER trg_ugyfel_kontroll
BEFORE UPDATE ON ugyfelek
FOR EACH ROW
BEGIN
    IF :NEW.husegpontok < 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Hiba: A hűségpont nem lehet negatív!');
    END IF;
END;
/


CREATE OR REPLACE TRIGGER trg_ugyfel_naplozas
AFTER INSERT OR UPDATE OR DELETE ON ugyfelek
FOR EACH ROW
DECLARE
    v_muvelet VARCHAR2(20);
    v_id NUMBER;
BEGIN
    IF INSERTING THEN
        v_muvelet := 'ÚJ FELVITEL';
        v_id := :NEW.ugyfel_id;
    ELSIF UPDATING THEN
        v_muvelet := 'MÓDOSÍTÁS';
        v_id := :NEW.ugyfel_id;
    ELSIF DELETING THEN
        v_muvelet := 'TÖRLÉS';
        v_id := :OLD.ugyfel_id;
    END IF;


    INSERT INTO ugyfel_naplo (log_id, muvelet_tipusa, erintett_ugyfel_id, muvelet_datuma)
    VALUES (seq_log_id.NEXTVAL, v_muvelet, v_id, SYSTIMESTAMP);
END;
/


CREATE OR REPLACE FUNCTION get_ugyfel_info(p_ugyfel_id IN NUMBER) RETURN VARCHAR2 IS
    v_nev ugyfelek.nev%TYPE;
    v_tel ugyfelek.telefonszam%TYPE;
BEGIN
    SELECT nev, telefonszam INTO v_nev, v_tel
    FROM ugyfelek 
    WHERE ugyfel_id = p_ugyfel_id;
    
    RETURN 'Név: ' || v_nev || ', Tel: ' || v_tel;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'Nincs ilyen ügyfél.';
END;
/

CREATE OR REPLACE FUNCTION get_ugyfel_osszkoltes(p_ugyfel_id IN NUMBER) RETURN NUMBER IS
    v_osszeg NUMBER;
BEGIN
    SELECT NVL(SUM(ar), 0) INTO v_osszeg
    FROM foglalasok
    WHERE ugyfel_id = p_ugyfel_id;
    
    RETURN v_osszeg;
END;
/

CREATE OR REPLACE PACKAGE szalloda_pkg IS
    PROCEDURE ugyfel_hozzadas(p_nev IN VARCHAR2, p_tel IN VARCHAR2, p_datum IN DATE);
    PROCEDURE ugyfel_pont_modositas(p_id IN NUMBER, p_uj_pont IN NUMBER);
    PROCEDURE ugyfel_torles(p_id IN NUMBER);
    PROCEDURE foglalasok_listazasa(p_ugyfel_id IN NUMBER); -- EXPLICIT KURZORHOZ
END szalloda_pkg;
/

CREATE OR REPLACE PACKAGE BODY szalloda_pkg IS

    PROCEDURE ugyfel_hozzadas(p_nev IN VARCHAR2, p_tel IN VARCHAR2, p_datum IN DATE) IS
    BEGIN
        INSERT INTO ugyfelek (nev, telefonszam, regisztracio_datuma) 
        VALUES (p_nev, p_tel, p_datum);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Ügyfél sikeresen rögzítve!');
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Hiba a felvitelkor: ' || SQLERRM);
            ROLLBACK;
    END ugyfel_hozzadas;

    PROCEDURE ugyfel_pont_modositas(p_id IN NUMBER, p_uj_pont IN NUMBER) IS
    BEGIN
        UPDATE ugyfelek SET husegpontok = p_uj_pont WHERE ugyfel_id = p_id;
        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Nincs ilyen ID-jú ügyfél!');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('Hűségpontok frissítve!');
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Hiba a módosításkor: ' || SQLERRM);
            ROLLBACK;
    END ugyfel_pont_modositas;

    PROCEDURE ugyfel_torles(p_id IN NUMBER) IS
    BEGIN
        DELETE FROM ugyfelek WHERE ugyfel_id = p_id;
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Ügyfél (és a hozzá tartozó foglalások) törölve.');
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Hiba törléskor: ' || SQLERRM);
            ROLLBACK;
    END ugyfel_torles;

    PROCEDURE foglalasok_listazasa(p_ugyfel_id IN NUMBER) IS
        CURSOR c_foglalasok IS 
            SELECT szobaszam, erkezes_datuma, ar 
            FROM foglalasok 
            WHERE ugyfel_id = p_ugyfel_id;
            
        v_rekord c_foglalasok%ROWTYPE;
    BEGIN
        DBMS_OUTPUT.PUT_LINE('--- Foglalások (Ügyfél ID: ' || p_ugyfel_id || ') ---');
        
        OPEN c_foglalasok;
        LOOP
            FETCH c_foglalasok INTO v_rekord;
            EXIT WHEN c_foglalasok%NOTFOUND;
            
            DBMS_OUTPUT.PUT_LINE('Szoba: ' || v_rekord.szobaszam || 
                                 ', Dátum: ' || TO_CHAR(v_rekord.erkezes_datuma, 'YYYY-MM-DD') || 
                                 ', Ár: ' || v_rekord.ar || ' Ft');
        END LOOP;
        CLOSE c_foglalasok;
        
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Hiba a listázás során: ' || SQLERRM);
    END foglalasok_listazasa;

END szalloda_pkg;
/