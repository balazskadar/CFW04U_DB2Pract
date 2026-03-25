SET SERVEROUTPUT ON;

DECLARE
    v_autok_szama NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_autok_szama
    FROM auto;

    DBMS_OUTPUT.PUT_LINE('Az adatbázisban jelenleg tárolt autók száma: ' || v_autok_szama || ' db');

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Hiba történt a darabszám lekérdezése során!');
END;
