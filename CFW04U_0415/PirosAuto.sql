DECLARE
    CURSOR piros IS
        SELECT rsz, tipus, szin, kor, ar
        FROM Piros_Auto
        WHERE szin = 'piros';
    x piros%ROWTYPE;
BEGIN
    OPEN piros;
    LOOP
        FETCH piros INTO x;
        EXIT WHEN piros%NOTFOUND;
        
        INSERT INTO MasikPiros_Auto (rsz, tipus, szin, kor, ar)
        VALUES (x.rsz, x.tipus, x.szin, x.kor, x.ar);
    END LOOP;
    CLOSE piros;
    COMMIT;
END;