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
	private static String DATABASE = "Checkpoint4.db";
	
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
			System.exit(1);
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
			String searchOption = "";
			System.out.println("Which would you like to search for?");
			System.out.println("a. Artist");
			System.out.println("b. Track");
			searchOption = scanner.nextLine();
			
			
			switch(searchOption){
				
				case "a":
				System.out.print("Enter the artist name: ");
				String artist_name = scanner.nextLine();
				Search.artist(conn, scanner, artist_name);

				break;
				case "b":
				System.out.print("Enter the track name: ");
				String track_name = scanner.nextLine();
				Search.track(conn, scanner, track_name);
				break;
				default:
				System.out.println("Invalid option.");
			}

			//search
			
			break;
			case "2":
			//add new records
			System.out.println("Which would you like to add?");
			System.out.println("a. Artist");
			System.out.println("b. Audiobook");
			searchOption = scanner.nextLine();
			
			switch(searchOption){
				
				case "a": // enter artist
				System.out.print("Enter the artist name: ");
				String artist_name = scanner.nextLine();
				System.out.print("Enter the artist's ID: ");
				String id_string = scanner.nextLine();
				int id = Integer.parseInt(id_string);
				System.out.print("Enter the artist's last active year: ");
				String active_year_string = scanner.nextLine();
				int active_year = Integer.parseInt(active_year_string);
				try{
				order_final.insertPerson(conn,id,artist_name);
				order_final.insertArtist(conn,id,active_year);
				}catch(SQLException e){
					e.printStackTrace();
				}

				break; 
				case "b": // enter audiobook

				System.out.print("Enter the book title: ");
				String title = scanner.nextLine();
				System.out.print("Enter the Item's ID: ");
				String item_id_string = scanner.nextLine();
				int item_id = Integer.parseInt(item_id_string);
				System.out.print("Enter the Item's inventory ID (1 or 2):");
				String inventory_id_string = scanner.nextLine();
				int inventory_id = Integer.parseInt(inventory_id_string);
				System.out.print("Enter the book's length in seconds: ");
				String length_string = scanner.nextLine();
				int length = Integer.parseInt(length_string);
				System.out.print("Enter the book's content rating: ");
				String content_rating = scanner.nextLine();
				System.out.print("Enter the book's release year: ");
				String year_string = scanner.nextLine();
				int year = Integer.parseInt(year_string);
				System.out.print("Enter the number of copies of the book: ");
				String copies_string = scanner.nextLine();
				int copies = Integer.parseInt(copies_string);
				System.out.print("Enter the book's genre: ");
				String genre = scanner.nextLine();
				System.out.print("Enter the number of chapters: ");
				String num_chap_string = scanner.nextLine();
				int num_chap = Integer.parseInt(num_chap_string);

				// public static void insertItem(Connection conn, int item_ID, int inventory_ID, int length, String content_rate,
// int year, String title, int num_copy) throws SQLException
			try{
				order_final.insertItem(conn,item_id,inventory_id,length,content_rating,year,title,copies); // seems to delete all other items from the database 
				order_final.insertAudiobook(conn,item_id,genre,num_chap);
			}catch(SQLException e){
				e.printStackTrace();
			}


				break;
				default:
				System.out.println("Invalid option.");
			}
			break;
			case "3":
			//order items
			System.out.println("What would you like to do?");
			System.out.println("a. Order a movie");
			System.out.println("b. Activate Item");
			searchOption = scanner.nextLine();
			
			switch(searchOption){
			case "a":
				// public static void insertOrder(Connection conn, int sysUser_ID, int order_ID, double price, Date arrive_date,
			// boolean isLate, int no_copies, int item_ID) throws SQLException 
			System.out.print("Enter the ordering system user's ID: ");
				String Sys_ID_s = scanner.nextLine();
				int Sys_ID = Integer.parseInt(Sys_ID_s);
				System.out.print("Enter the order ID: ");
				String order_ID_s = scanner.nextLine();
				int order_ID = Integer.parseInt(order_ID_s);
				System.out.print("Enter the price of the order: ");
				String price_s = scanner.nextLine();
				double price = Double.parseDouble(price_s);
				//TODO: Ask user for date and convert to correct format
				System.out.print("Is the order late?: ");
				String isLate_s = scanner.nextLine();
				boolean is_Late;
				switch(isLate_s){
					case "y":
					case "yes":
						is_Late = true;
					break;
					default:
					is_Late = false;
				}
				System.out.print("Enter the number of copies: ");
				String copies_s = scanner.nextLine();
				int copies = Integer.parseInt(copies_s);
				System.out.print("Enter the ordered item's ID: ");
				String item_id_s = scanner.nextLine();
				int item_id = Integer.parseInt(item_id_s);
				order_final.insertOrder(conn,Sys_ID,order_ID,price,/*TODO: DATE*/,is_Late,copies,item_id);
			break;
			case "b":
			//Activate item received
			break;
			default:
			System.out.println("Invalid option.");
			System.exit(1);
			}
			break;
			case "4":
			//edit records
			
			break;
			case "5":
			//useful reports
			Reports.reports(conn);
			break;
			default:
			System.out.println("Invalid option.");
			System.exit(1);
		}
		try{
		conn.close();
		scanner.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}