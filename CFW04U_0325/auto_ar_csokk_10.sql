SET SERVEROUTPUT ON;

DECLARE
    v_kor_hatar auto.kor%TYPE := 7;
    v_sorok_szama NUMBER;
BEGIN
    UPDATE auto
    SET ar = ar * 0.9
    WHERE kor > v_kor_hatar;

    v_sorok_szama := SQL%ROWCOUNT;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(v_sorok_szama || ' darab 7 évnél idősebb autó ára csökkentve 10%-kal.');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Hiba történt a frissítés során!');
        ROLLBACK;
END;
