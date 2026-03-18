import java.util.Scanner;

public class ConsoleMethods {

    private Scanner scanner = new Scanner(System.in);

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
public int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Hibás szám, próbáld újra.");
            }
        }
    }

    
}