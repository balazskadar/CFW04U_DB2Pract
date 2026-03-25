DECLARE
    a NUMBER := 12;
    b NUMBER := 6;
    szorzat NUMBER;
BEGIN
    szorzat := a * b;
    DBMS_OUTPUT.PUT_LINE('A szorzat: ' || szorzat);
END;