package CFW04U;


public class PROJECT {
	
	//static DBMethods DBm 7 new DBMethods();
	
public static void main(String[] args) {
	DBMethods.Register();
	DBMethods.ReadAllData();
	
	System.out.println("\n-----------------------------------\n");
	DBMethods.ReadTulajdonosData(); 
	
}
}
