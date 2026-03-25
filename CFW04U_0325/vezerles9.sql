DECLARE
    n NUMBER := 10;
    prim BOOLEAN := TRUE;
BEGIN
    IF n < 2 THEN
        prim := FALSE;
    ELSE
        FOR i IN 2..TRUNC(SQRT(n)) LOOP
            IF MOD(n, i) = 0 THEN
                prim := FALSE;
                EXIT;
            END IF;
        END LOOP;
    END IF;
 
    IF prim THEN
        DBMS_OUTPUT.PUT_LINE(n || ' prímszám.');
    ELSE
        DBMS_OUTPUT.PUT_LINE(n || ' nem prímszám.');
    END IF;
END;