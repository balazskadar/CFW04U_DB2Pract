/*
8. feladat:
LOOP, WHILE és FOR ciklus bemutatása
*/
 
BEGIN

    DBMS_OUTPUT.PUT_LINE('--- LOOP ciklus ---');
 
    -- LOOP: addig fut, amíg ki nem lépünk belőle

    DECLARE

        i NUMBER := 1;

    BEGIN

        LOOP

            DBMS_OUTPUT.PUT_LINE('LOOP: ' || i);

            i := i + 1;
 
            EXIT WHEN i > 5; -- itt lépünk ki

        END LOOP;

    END;
 
    DBMS_OUTPUT.PUT_LINE('--- WHILE ciklus ---');
 
    -- WHILE: addig fut, amíg a feltétel igaz

    DECLARE

        i NUMBER := 1;

    BEGIN

        WHILE i <= 5 LOOP

            DBMS_OUTPUT.PUT_LINE('WHILE: ' || i);

            i := i + 1;

        END LOOP;

    END;
 
    DBMS_OUTPUT.PUT_LINE('--- FOR ciklus ---');
 
    -- FOR: automatikusan végigmegy a tartományon

    BEGIN

        FOR i IN 1..5 LOOP

            DBMS_OUTPUT.PUT_LINE('FOR: ' || i);

        END LOOP;

    END;
 
END;

/
 
9. DECLARE

    n NUMBER := 10;

BEGIN

    FOR i IN 1..n LOOP

        DBMS_OUTPUT.PUT_LINE(i);

    END LOOP;

END;

/
 