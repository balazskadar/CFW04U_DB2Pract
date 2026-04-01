
CREATE TABLE Circles (
    RADIUS NUMBER(4) PRIMARY KEY,
    CIRCUMFERENCE NUMBER,
    AREA NUMBER
);

DESC cIRCLES

select 1 Radius, 2*1*PI() CIRCUMFERENCE from dual;

//kör kerülete
 DECLARE
 Radius number:= 1;
    Circumference number;
BEGIN
    Circumference := 2*Radius*PI();
    dbms_output.put_line('Radius: '||Radius||', Circumference: '||Circumference);
END;

DECLARE

Circumference number;
X NUMBER:= 1;
y NUMBER:= 5;
BEGIN
   FOR i IN x..y LOOP
    Circumference := 2*i*PI();
    dbms_output.put_line('Radius: '||i||', Circumference: '||Circumference);
   END LOOP;
END;

//több 1...5 sugarú kör kerülete eljárással
CREATE OR REPLACE PROCEDURE Circler (x in number, y in number) IS
Circumference number;
BEGIN
   FOR i IN x..y LOOP
    Circumference := 2*i*PI();
    dbms_output.put_line('Radius: '||i||', Circumference: '||Circumference);
   END LOOP;
END;

BEGIN
    Circler(1,5);
END;

//a kör kerülete és területének kiszALTERámítása eljárással
CREATE OR REPLACE PROCEDURE CircleCalc (x in number, y in number) IS
Circumference number;
Area number;
BEGIN
   FOR i IN x..y LOOP
    Circumference := 2*i*PI();
    Area := POWER(i,2)*PI();
    dbms_output.put_line('Radius: '||i||', Circumference: '||Circumference||', Area: '||Area);
   END LOOP;
END;
// a kör adatainak beszúrása a táblával
create or replace procedure Circler(x in number, y in number) IS 
Circumference number;
Area number;
BEGIN
   FOR i IN x..y LOOP
    Circumference := 2*i*PI();
    Area := POWER(i,2)*PI();
    INSERT INTO CIrcles VALUES (i, Circumference, Area);
   END LOOP;
    dbms_output.put_line('Radius: '||i||', Circumference: '||Circumference||', Area: '||Area);
END;

SELECT * FROM Circles;





    
