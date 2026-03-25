DECLARE
    a NUMBER := 3;
    b NUMBER := 4;
    c NUMBER := 5;
BEGIN
    IF a + b > c AND a + c > b AND b + c > a THEN
        DBMS_OUTPUT.PUT_LINE('A három szám alkothat háromszöget.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('A három szám nem alkothat háromszöget.');
    END IF;
END;