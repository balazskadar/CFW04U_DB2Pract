SET SERVEROUTPUT ON;

DECLARE
    v_atlag_ar NUMBER(12, 2);
BEGIN
    SELECT AVG(ar)
    INTO v_atlag_ar
    FROM auto;

    IF v_atlag_ar IS NOT NULL THEN
        DBMS_OUTPUT.PUT_LINE('Az autók átlagos ára: ' || ROUND(v_atlag_ar, 0) || ' Ft');
    ELSE
        DBMS_OUTPUT.PUT_LINE('A tábla üres, nem számolható átlagár.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Hiba történt a lekérdezés során!');
END;
