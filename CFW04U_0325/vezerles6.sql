DECLARE
    a NUMBER := 3;
    b NUMBER := 4;
    c NUMBER := 5;
    s NUMBER;
    t NUMBER;
BEGIN
    s := (a + b + c) / 2;
    t := SQRT(s * (s - a) * (s - b) * (s - c));
 
    DBMS_OUTPUT.PUT_LINE('A háromszög félkerülete: ' || s);
    DBMS_OUTPUT.PUT_LINE('A háromszög területe: ' || t);
END;