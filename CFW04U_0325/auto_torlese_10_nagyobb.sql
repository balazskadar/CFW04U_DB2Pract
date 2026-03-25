CREATE OR REPLACE PROCEDURE auto_torlese_10_nagyobb AS
BEGIN
    DELETE FROM auto
    WHERE kor > 10;

    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('A 10 évnél idősebb autók törlése sikeresen megtörtént.');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Hiba történt a törlés során!');
        ROLLBACK;
END;
