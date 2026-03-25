import java.util.Scanner;
 
public class Program {
   
    private static int readInt(Scanner sc,String prompt){
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Hibás szám ,kérem próbálja újra.");
            }
        }
    }
 
    private static boolean readBoolean(Scanner sc,String prompt){
       while (true) {
           System.out.print(prompt);
        String line = sc.nextLine().trim().toLowerCase();
        if(line.equals("true") || line.equals("t") || line.equals("igen") || line.equals("i") ) return true;
        else if(line.equals("false") || line.equals("f") || line.equals("nem") || line.equals("n") ) return false;
        System.out.println("Hibás érték! Írd: true/false.");
    }
 }
 
 private static String readText(Scanner sc,String prompt){
    System.out.print(prompt);
    return sc.nextLine().trim();
 }
 
 public static void main(String[] args){
   DbMethods.Register();
 
   Scanner sc = new Scanner(System.in);
   boolean running = true;
 
    while (running) {
      System.out.println("\nFŐMENÜ");
      System.out.println("1 - Rekordok száma - task_a");
      System.out.println("2 - Oszlop összeg és átlag - task_b");
      System.out.println("3 - Rendezett lista - task_c");
      System.out.println("4 - Hallgatók projektjeinek száma - task_d");
      System.out.println("5 - Tábla listázása");
      System.out.println("0 - Kilépés");  
     
        int choice = readInt(sc, "Válassz egy menüpontot: ");
        switch (choice) {
            case 1:
                String tableA = readText(sc, " tábla neve: ");
                ListingMethods.task_a(tableA);
                break;
            case 2:
                String tableB = readText(sc, " tábla neve: ");
                String columnB = readText(sc, " oszlop neve: ");
                ListingMethods.task_b(tableB, columnB);
                break;
            case 3:
                String tableC = readText(sc, " tábla neve: ");
                String columnC = readText(sc, " oszlop neve: ");
                boolean ascending = readBoolean(sc, "Növekvő sorrend? (true/false): ");
                ListingMethods.task_c(tableC, columnC, ascending);
                break;
            case 4:
                ListingMethods.task_d();
                break;
            case 5:
                System.out.println("Tábla kiválasztása");
                System.out.println("1 - hallgato");
                System.out.println("2 - projekt");
                System.out.println("3 - dolgozik");
                int t = readInt(sc, "Választás: ");
                if(t == 1) ListingMethods.listTables("Hallgato");
                else if(t == 2) ListingMethods.listTables("Projekt");
                else if(t == 3) ListingMethods.listTables("Dolgozik");
                else System.out.println("Érvénytelen választás.");
                break;
            case 0:
                running = false;
                System.out.println("Program vége.");
                break;
            default:
                System.out.println("Érvénytelen választás. Próbáld újra.");
        }
    }
    sc.close();
 
 }
       
}