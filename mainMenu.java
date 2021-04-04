import java.sql.*;
import java.util.*;

class mainMenu{
/**
	 *  The database file name.
	 *  
	 *  Make sure the database file is in the root folder of the project if you only provide the name and extension.
	 *  
	 *  Otherwise, you will need to provide an absolute path from your C: drive or a relative path from the folder this class is in.
	 */
	private static String DATABASE = "C:\\Users\\Michael\\Desktop\\2021 Spring\\Databases 1\\Embedded SQL Lab\\database_binary.db";
	
    /**
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
    }
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
    	String option = "";

    	System.out.println("Attempting connection to database...");
    	Connection conn = initializeDB(DATABASE);
		System.out.print("\n\n\n");

		System.out.println("Please enter a number to select a database operation.");
		System.out.println("1. Search");
		System.out.println("2. Add new records");
		System.out.println("3. Order Items");
		System.out.println("4. Edit Records");
		System.out.println("5. Useful reports\n");
		option = scanner.nextLine();
		
		switch(option){
			case "1":
			//search
			
			break;
			case "2":
			//add new records
			
			break;
			case "3":
			//order items
			
			break;
			case "4":
			//edit records
			
			break;
			case "5":
			//useful reports

			break;
			default:
			
		}
		try{
		conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}