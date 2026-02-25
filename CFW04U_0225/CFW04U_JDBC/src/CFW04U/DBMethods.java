package CFW04U;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBMethods {

	final static String url = "jdbc:sqlite:C:\\Users\\egyetem\\Documents\\GitHub\\CFW04U_adatbazis2\\CFW04U_DB2Pract\\CFW04U_0225\\CFW04U_sql3";
	
	
	
	public static void Register() {
		try {
			
		
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found exception: " + e.getMessage());
		}
	
	
	public static Connection Connect(); {
	Connection conn = null;
	try {
		conn = Drivermanager.getConnection(url);
	}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	return conn;
	}
	
	public static void Disconnect(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void Commandexec(String command) {
		Connection conn = Connect();
		try {
			Statement s = conn.createStatement();
			s.execute(command);
		}
		catch (SQLException e) {
			System.out.println("Command: " + command);
			System.out.println(e.getMessage());
		}
		DisConnect(conn);
	}
	
	public static void ReadAllData() {
		String rendszam = "";
		String tipus = "";
		String szin = "";
		String tulaj = "";
		int kor = 0;
		int ar = 0;
		String sqlp = "SELECT Rendszam, Tipus, Szin, Kor, Ar, Tulaj FROM Auto";
		Connection conn = Connect();
		
		System.out.println("Autó tábla\n");
		
		try {
			Statement statement = conn.createStatement();
			ResultSet resoult_set = statement.executeQuery(sqlp);
			while(resoult_set.next()) {
				rendszam = resoult_set.getString("Rendszám");
				tipus = resoult_set.getString("Típus");
				szin = resoult_set.getString("Szín");
				kor = resoult_set.getInt("Kor");
				ar = resoult_set.getint("Ár");
				tulaj = resoult_set.getString("Tulaj");
				System.out.println(
						rendszam + "\t" +
								tipus + "\t" +
								szin + "\t" +
								kor + "\t" +
								ar + "\t" +
								tulaj
								);
			}
			resoult_set.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		DisConnect(conn);
		}
	}
	
	
	
	
	
	
	
	

