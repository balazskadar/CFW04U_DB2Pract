package CFW04U;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    // Adatbázis 
    private static final String URL = "jdbc:sqlite:szalloda.db";
    private static Scanner scanner = new Scanner(System.in);
    private static Connection conn = null;

    public static void main(String[] args) {
        if (connect()) {
            createTables(); // Táblák létrehozása, ha nem léteznek
            insertDefaultAdmin(); // Alap admin létrehozása teszteléshez
            
            //Bejelentkezési modul
            if (login()) {
                System.out.println("Sikeres bejelentkezés!\n");
                showMenu();
            } else {
                System.out.println("Hibás felhasználónév vagy jelszó. Kilépés.");
            }
            
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Kapcsolódás az adatbázishoz
    private static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            
            conn = DriverManager.getConnection(URL);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Hiba: Nem található az SQLite driver! " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Hiba a kapcsolódáskor: " + e.getMessage());
            return false;
        }
    }

    // Táblák inicializálása 
    private static void createTables() {
        String adminTable = "CREATE TABLE IF NOT EXISTS adminok (admin_id INTEGER PRIMARY KEY AUTOINCREMENT, felhasznalonev TEXT, jelszo TEXT);";
        String ugyfelTable = "CREATE TABLE IF NOT EXISTS ugyfelek (ugyfel_id INTEGER PRIMARY KEY AUTOINCREMENT, nev TEXT, telefonszam TEXT, husegpontok INTEGER, regisztracio_datuma TEXT);";
        String foglalasTable = "CREATE TABLE IF NOT EXISTS foglalasok (foglalas_id INTEGER PRIMARY KEY AUTOINCREMENT, ugyfel_id INTEGER, szobaszam INTEGER, erkezes_datuma TEXT, ar INTEGER, FOREIGN KEY (ugyfel_id) REFERENCES ugyfelek(ugyfel_id));";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(adminTable);
            stmt.execute(ugyfelTable);
            stmt.execute(foglalasTable);
        } catch (SQLException e) {
            System.out.println("Hiba a táblák létrehozásakor: " + e.getMessage());
        }
    }

    private static void insertDefaultAdmin() {
        String query = "SELECT count(*) AS db FROM adminok";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next() && rs.getInt("db") == 0) {
                stmt.execute("INSERT INTO adminok (felhasznalonev, jelszo) VALUES ('admin', 'admin123')");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    

    // Bejelentkezés megvalósítása
    private static boolean login() {
        System.out.println("--- BEJELENTKEZÉS ---");
        System.out.print("Felhasználónév (teszt: admin): ");
        String user = scanner.nextLine();
        System.out.print("Jelszó (teszt: admin123): ");
        String pass = scanner.nextLine();

        String sql = "SELECT * FROM adminok WHERE felhasznalonev = ? AND jelszo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Ha van találat, igazat ad vissza
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- FŐMENÜ ---");
            System.out.println("1. Új ügyfél felvitele (Dátum ellenőrzéssel)");
            System.out.println("2. Ügyfelek és foglalásaik lekérdezése (Join és több mezős szűrés)");
            System.out.println("3. Ügyfél adatainak módosítása");
            System.out.println("4. Ügyfél törlése");
            System.out.println("5. Kilépés");
            System.out.print("Válassz: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // buffer ürítés

            switch (choice) {
                case 1: addUgyfel(); break;
                case 2: queryAdatok(); break;
                case 3: modifyUgyfel(); break;
                case 4: deleteUgyfel(); break;
                case 5: exit = true; break;
                default: System.out.println("Érvénytelen választás.");
            }
        }
    }

    // 1. Új adat felvitele 
    private static void addUgyfel() {
        System.out.print("Ügyfél neve: ");
        String nev = scanner.nextLine();
        System.out.print("Telefonszám: ");
        String tel = scanner.nextLine();
        System.out.print("Hűségpontok (szám): ");
        int pont = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Regisztráció dátuma (ÉÉÉÉ-HH-NN formátumban): ");
        String datum = scanner.nextLine();

        // Dátum validáció 
        if (!isValidDate(datum)) {
            System.out.println("HIBA: Helytelen dátum formátum vagy nem létező dátum! A felvitel megszakítva.");
            return;
        }

        String sql = "INSERT INTO ugyfelek (nev, telefonszam, husegpontok, regisztracio_datuma) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nev);
            pstmt.setString(2, tel);
            pstmt.setInt(3, pont);
            pstmt.setString(4, datum);
            pstmt.executeUpdate();
            System.out.println("Ügyfél sikeresen rögzítve!");
        } catch (SQLException e) {
            System.out.println("Hiba a felvitelkor: " + e.getMessage());
        }
    }

    // Dátum ellenőrző segédfüggvény
    private static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); 
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // 2. Adatok lekérdezése 
    private static void queryAdatok() {
        System.out.println("\n--- LEKÉRDEZÉS ---");
        System.out.print("Keresett ügyfél neve (hagyjd üresen, ha mindenkit látni akarsz): ");
        String nevKeres = scanner.nextLine();
        
        // Két tábla összekapcsolása (JOIN), szűrés névre (LIKE)
        String sql = "SELECT u.ugyfel_id, u.nev, u.husegpontok, f.szobaszam, f.erkezes_datuma " +
                     "FROM ugyfelek u LEFT JOIN foglalasok f ON u.ugyfel_id = f.ugyfel_id " +
                     "WHERE u.nev LIKE ?";
                     
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nevKeres + "%");
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\nEredmények:");
            System.out.printf("%-5s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Név", "Pontok", "Szobaszám", "Érkezés");
            System.out.println("-----------------------------------------------------------------------");
            
            boolean vanEredmeny = false;
            while (rs.next()) {
                vanEredmeny = true;
                System.out.printf("%-5d | %-20s | %-10d | %-10s | %-15s\n",
                        rs.getInt("ugyfel_id"),
                        rs.getString("nev"),
                        rs.getInt("husegpontok"),
                        (rs.getInt("szobaszam") == 0 ? "Nincs" : rs.getInt("szobaszam")),
                        (rs.getString("erkezes_datuma") == null ? "-" : rs.getString("erkezes_datuma")));
            }
            if (!vanEredmeny) System.out.println("Nincs találat a megadott feltételekkel.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. Adatok módosítása
    private static void modifyUgyfel() {
        System.out.print("Módosítandó ügyfél ID-ja: ");
        int id = scanner.nextInt();
        System.out.print("Új hűségpont mennyiség: ");
        int ujPont = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE ugyfelek SET husegpontok = ? WHERE ugyfel_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ujPont);
            pstmt.setInt(2, id);
            int erintettSorok = pstmt.executeUpdate();
            
            if(erintettSorok > 0) {
                System.out.println("Sikeres módosítás!");
            } else {
                System.out.println("Nem található ügyfél ezzel az ID-val.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. Adatok törlése
    private static void deleteUgyfel() {
        System.out.print("Törlendő ügyfél ID-ja: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM ugyfelek WHERE ugyfel_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int erintettSorok = pstmt.executeUpdate();
            
            if(erintettSorok > 0) {
                System.out.println("Ügyfél sikeresen törölve!");
            } else {
                System.out.println("Nem található ügyfél ezzel az ID-val.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}