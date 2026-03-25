DECLARE
    min_ertek NUMBER := 10;
    max_ertek NUMBER := 100;
    ertek NUMBER := 78;
BEGIN
    IF ertek >= min_ertek AND ertek <= max_ertek THEN
        DBMS_OUTPUT.PUT_LINE('Az érték beleesik az intervallumba.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Az érték nem esik bele az intervallumba.');
    END IF;
END;