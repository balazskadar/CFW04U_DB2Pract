DECLARE
    CURSOR piros IS
        SELECT rsz, tipus, szin, kor, ar
        FROM Piros_Auto
        WHERE szin = 'piros';
BEGIN
    -- A FOR ciklus automatikusan deklarálja az 'x' változót és kezeli a kurzort
    FOR x IN piros LOOP
        INSERT INTO MasikPiros_Auto (rsz, tipus, szin, kor, ar)
        VALUES (x.rsz, x.tipus, x.szin, x.kor, x.ar);
    END LOOP;
    COMMIT;
END;