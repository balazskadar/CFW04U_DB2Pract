DECLARE
    beosztas VARCHAR2(20) := 'root';
BEGIN
    CASE beosztas
        WHEN 'root' THEN
            DBMS_OUTPUT.PUT_LINE('Rendszergazda (UNIX/LINUX)');
        WHEN 'dba' THEN
            DBMS_OUTPUT.PUT_LINE('Adatbázis-adminisztrátor');
        WHEN 'dev' THEN
            DBMS_OUTPUT.PUT_LINE('Fejlesztő');
        WHEN 'hr' THEN
            DBMS_OUTPUT.PUT_LINE('Humán erőforrás munkatárs');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Ismeretlen beosztás');
    END CASE;
END;