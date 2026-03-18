import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ListingMethods {

    //task_a: A rekord számainak kiírása táblából
    public static void task_a(String table_name) {
        String sqlp = "SELECT COUNT(*) AS db FROM " + qIdent(table_name);
        //quote identifier - a tábla formázása, table_name: Dolgozik
        Connection conn = DbMethods.Connect();
        ResultSet result_set = execute(conn, sqlp);

        if (result_set != null) {
            try {
                if (result_set.next()) {
                    int count = result_set.getInt("db");
                    System.out.println(table_name + " tábla rekordjainak száma: " + count);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        DbMethods.DisConnect(conn);
    }

    // task_b: Hallgato tabla ID mező szerint SUM és AVG készítése
    public static void task_b(String table_name, String column) {
        String sqlp = "SELECT SUM(" + qIdent(column) + ") AS sum, AVG(" + qIdent(column) + ") AS avg FROM " + qIdent(table_name);
        Connection conn = DbMethods.Connect();
        ResultSet result_set = execute(conn, sqlp);

        if (result_set != null) {
            try {
                if (result_set.next()) {
                    // SUM lehet NULL, ha nincs sor
                    Object sumObj = result_set.getObject("sum");
                    double avg = result_set.getDouble("avg");

                    String sumStr = (sumObj == null) ? "null" : sumObj.toString();
                    System.out.println(table_name + " tábla " + column + " oszlopának összege: " + sumStr + ", átlaga: " + avg);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        DbMethods.DisConnect(conn);
    }

    // task_c: A Hallgato tábla rendezett listázása: mező és rendezés paraméterrel
    // ascending=true -> ASC, false -> DESC
    public static void task_c(String table_name, String column, boolean ascending) {
        String sqlp = "SELECT * FROM " + qIdent(table_name) + " ORDER BY " + qIdent(column) + (ascending ? " ASC" : " DESC");
        Connection conn = DbMethods.Connect();
        ResultSet result_set = execute(conn, sqlp);

        if (result_set != null) {
            try {
                ResultSetMetaData md = result_set.getMetaData();
                int column_count = md.getColumnCount();

                // fejléc
                String header = "";
                for (int i = 1; i <= column_count; i++) {
                    header += md.getColumnName(i) + "\t";
                }
                System.out.println(header);

                while (result_set.next()) {
                    String row = "";
                    for (int i = 1; i <= column_count; i++) {
                        row += result_set.getString(i) + "\t";
                    }
                    System.out.println(row);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        DbMethods.DisConnect(conn);
    }

    // task_d: 3 tábla JOIN + GROUP BY: hallgató -> projektek száma
    // Hallgato(ID, Vnev, Knev, SzulI, Lakcim)
    // Dolgozik(ID, Pkod)
    // Projekt(Pkod, Nev, Osszertek, Indul, Zarul)
    public static void task_d() {
        String sqlp = 
                "SELECT h.Vnev, h.Knev, COUNT(d.Pkod) AS db " +
                "FROM Hallgato h " +
                "INNER JOIN Dolgozik d ON h.ID = d.ID " +
                "INNER JOIN Projekt p ON d.Pkod = p.Pkod " +
                "GROUP BY h.Vnev, h.Knev " +
                "ORDER BY db DESC, h.Vnev ASC, h.Knev ASC";
        //Hozzákapcsolja a "Dolgozik" táblát, ahol a hallgató azonosítója (ID) megegyezik.
        // Hallgatók tábla vezeték és keresztnév alapján végezzük a csoportosítást.

        Connection conn = DbMethods.Connect();
        ResultSet result_set = execute(conn, sqlp);

        if (result_set != null) {
            try {
                while (result_set.next()) {
                    String vnev = result_set.getString("Vnev");
                    String knev = result_set.getString("Knev");
                    int count = result_set.getInt("db");
                    System.out.println("Név: " + vnev + " " + knev + "\t Projektek száma: " + count);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        DbMethods.DisConnect(conn);
    }

    // Tábla teljes listázása fejléc + sorok
    public static void listTable(String table_name) {
        System.out.println("\n" + table_name);
        String sqlp = "SELECT * FROM " + qIdent(table_name);
        Connection conn = DbMethods.Connect();
        ResultSet result_set = execute(conn, sqlp);

        if (result_set != null) {
            try {
                ResultSetMetaData md = result_set.getMetaData();
                int column_count = md.getColumnCount();

                String header = "";
                for (int i = 1; i <= column_count; i++) {
                    header += md.getColumnName(i) + "\t";
                }
                System.out.println(header);

                while (result_set.next()) {
                    String row = "";
                    for (int i = 1; i <= column_count; i++) {
                        row += result_set.getString(i) + "\t";
                    }
                    System.out.println(row);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        DbMethods.DisConnect(conn);
    }

    // Segédfüggvények
    private static ResultSet execute(Connection conn, String sqlp) {
        ResultSet result_set = null;
        try {
            Statement statement = conn.createStatement();
            result_set = statement.executeQuery(sqlp);
        } catch (SQLException e) {
            System.out.println("SQL hiba: " + e.getMessage());
        }
        return result_set;
    }

    // biztonságos azonosító (táblanév / oszlopnév) ellenőrzés és idézőjel használata SQLite-ban
    private static String qIdent(String ident) {
        if (ident == null || !ident.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("Nem megengedett azonosító: " + ident);
        }
        return "\"" + ident + "\"";
    }
}