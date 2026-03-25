DECLARE
    vezeteknev VARCHAR2(50) := 'Vezetéknév';
    keresztnev VARCHAR2(50) := 'Keresztnév';
    teljes_nev VARCHAR2(100);
BEGIN
    teljes_nev := vezeteknev || ' ' || keresztnev;
    DBMS_OUTPUT.PUT_LINE(teljes_nev);
END;