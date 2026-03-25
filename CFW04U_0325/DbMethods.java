import java.sql.*;

public class DbMethods {
    
    // IDE KERÜLT BE A MAIN METÓDUS AZ OSZTÁLYON BELÜLRE:
    public static void main(String[] args) {
        System.out.println("Adatbázis kapcsolat tesztelése...");
        Register(); 
        ReadAllData(); 
    }

    final static String url = "jdbc:sqlite:C:\\Users\\egyetem\\Documents\\GitHub\\CFW04U_adatbazis2\\CFW04U_DB2Pract\\CFW04U_0318\\CFW04U_sql3";
    
    public static void Register() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception: " + e.getMessage());
        }
    }
    
    public static Connection Connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void DisConnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    // Hallgato tábla teljes kiolvasása
    public static void ReadAllData() {
        int id;
        String vnev;
        String knev;
        String szuli;
        String lakcim;
        String sqlp = "SELECT ID, Vnev, Knev, SzulI, Lakcim FROM Hallgato";
        Connection conn = Connect();
        
        // Ellenőrizzük, hogy sikerült-e csatlakozni, mielőtt lekérdezünk
        if (conn == null) {
            System.out.println("Nem sikerült csatlakozni az adatbázishoz, a lekérdezés megszakítva.");
            return;
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet result_set = statement.executeQuery(sqlp);
            while (result_set.next()) {
                id = result_set.getInt("ID");
                vnev = result_set.getString("Vnev");
                knev = result_set.getString("Knev");
                szuli = result_set.getString("SzulI");
                lakcim = result_set.getString("Lakcim");
                System.out.println(
                        id + "\t" +
                        vnev + "\t" +
                        knev + "\t" +
                        szuli + "\t" +
                        lakcim
                );
            }
            result_set.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL Hiba a lekérdezéskor: " + e.getMessage());
        } finally {
            DisConnect(conn);
        }
    }
}