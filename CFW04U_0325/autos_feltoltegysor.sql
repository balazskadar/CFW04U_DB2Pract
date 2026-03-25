DECLARE
    v_rsz   auto.rsz%TYPE   := 'ABC-123';
    v_tipus auto.tipus%TYPE := 'Opel Astra';
    v_szin  auto.szin%TYPE  := 'Ezüst';
    v_kor   auto.kor%TYPE   := 5;
    v_ar    auto.ar%TYPE    := 3500000;
BEGIN
    INSERT INTO auto (rsz, tipus, szin, kor, ar)
    VALUES (v_rsz, v_tipus, v_szin, v_kor, v_ar);

    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Az autó sikeresen felvéve a rendszerbe!');
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Hiba: Ez a rendszám már létezik!');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Váratlan hiba történt!');
        ROLLBACK;
END;
