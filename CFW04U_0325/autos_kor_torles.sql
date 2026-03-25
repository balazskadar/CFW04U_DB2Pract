SET SERVEROUTPUT ON;

DECLARE
    v_limit_kor NUMBER := 10;
    v_torolt_db NUMBER;
BEGIN
    DELETE FROM auto
    WHERE kor > v_limit_kor;

    v_torolt_db := SQL%ROWCOUNT;

    COMMIT;

    IF v_torolt_db > 0 THEN
        DBMS_OUTPUT.PUT_LINE('Sikeresen törölve lett ' || v_torolt_db || ' darab 10 évnél idősebb autó.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Nem találtam 10 évnél idősebb autót a táblában.');
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Hiba történt a törlés során!');
        ROLLBACK;
END;
