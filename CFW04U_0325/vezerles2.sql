DECLARE
    a NUMBER := 14;
    b NUMBER := 61;
BEGIN
    IF a > b THEN
        DBMS_OUTPUT.PUT_LINE('A nagyobb szám: ' || a);
    ELSIF b > a THEN
        DBMS_OUTPUT.PUT_LINE('A nagyobb szám: ' || b);
    ELSE
        DBMS_OUTPUT.PUT_LINE('A két szám egyenlő.');
    END IF;
END;