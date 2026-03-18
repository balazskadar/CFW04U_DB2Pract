public class Program {
    public static void main(String[] args) {
        ConsoleMethods cm= new ConsoleMethods();
        DbMethod.Register();

        while(true){
            System.out.println("\nAUTÓ ADATBÁZIS ");
            System.out.println("1 - ÚJ rekord beszúrása");
            System.out.println("2 - Ár modositása redszám alapján");
            System.out.println("3 - Törlés rendszám alapján");
            System.out.println("4 - Összes rekord megjelenítése");
            System.out.println("0 - Kilépés");

            int choice=cm.readInt("Válassz egy műveletet: ");

            switch(choice){
                case 1:
                    String rendszam = cm.readString("Kérem a redszámot: ");
                    String tipus = cm.readString("Kérem a tipusát: ");
                    String szin = cm.readString("Kérem a színét: ");
                    int kor = cm.readInt("Kérem a korát: ");
                    int ar = cm.readInt("Kérem az árát: ");
                    String tulaj = cm.readString("Kérem a tulajdonos nevét: ");
                    DbMethod.Insert(rendszam, tipus, szin, kor, ar, tulaj);
                    break;
                case 2:
                    String r2 = cm.readString("Kérem a redszámot: ");
                    int ujAr = cm.readInt("Kérem az új árat: ");
                    DbMethod.Update(r2, ujAr);
                    break;
                case 3:
                    String r3 = cm.readString("Kérem a rendszámot: ");
                    DbMethod.DeleteData(r3);
                    break;
                case 4:
                    DbMethod.ReadAllData();
                    break;
                case 0:
                    System.out.println("Kilépés...");
                    return;
                default:
                    System.out.println("Érvénytelen választás, próbáld újra.");
                    break;
            }
        }

    }
    
}
