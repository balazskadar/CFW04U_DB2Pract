DECLARE
    a NUMBER := 10;
    b NUMBER := 5;
    osszeg NUMBER;
BEGIN
    osszeg := a + b;
    DBMS_OUTPUT.PUT_LINE('Az összeg: ' || osszeg);
END;