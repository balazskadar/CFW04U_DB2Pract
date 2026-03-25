DECLARE
    szoveg VARCHAR2(100) := 'Kádár Balázs';
BEGIN
    DBMS_OUTPUT.PUT_LINE('Nagybetűvel: ' || UPPER(szoveg));
    DBMS_OUTPUT.PUT_LINE('Kisbetűvel: ' || LOWER(szoveg));
END;