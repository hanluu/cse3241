
import java.util.*;
import java.sql.*;
public class EditRecord {

    // Edit records 
    // Edit an Artist 

	// static final String DB_URL = "jdbc:sqlite:Library.db";
	public static void Edit(Connection conn, Scanner scanner ) throws SQLException
	{    
        // this.conn = conn;
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		try {
			// System.out.println("Connecting to database...");
			// conn = DriverManager.getConnection(DB_URL);
            System.out.println("Here is existing data of artists: ");
			String sql = 
            "Select Person.*, Artist.active_year "+
            "from Artist "+
            "left join Person on Artist.Person_ID = Person.Person_ID;";
			stmt = conn.prepareStatement(sql);
			// stmt.setInt(1, 10);
			rSet = stmt.executeQuery();
            ShowResult(rSet);

            System.out.println("Choose an artist by entering their Person_ID.");
            // Scanner input2 = new Scanner(System.in); 
            System.out.print("ArtistId: ");
            String artist = scanner.nextLine();
			System.out.println("What would you like to do?");
            System.out.println("1. Edit artist's name.");
			System.out.println("2. Edit active year please.");
            System.out.println("3. Edit both artist's name and active year.");

            // Scanner temp1 = new Scanner(System.in); 
			String which = scanner.nextLine();
            
            
            try{
                String update = "";
                
                if(which.equals("1")){
				    System.out.print("What is the artist's new name? ");

                    String edit = scanner.nextLine();
                    update = "update Person set Name = ? where Person.Person_ID = ?;";
                    stmt = null;
                    stmt = conn.prepareStatement(update);
                    stmt.setString(1, edit);
                    stmt.setInt(2, Integer.parseInt(artist));
                    System.out.println("Editing ...");
	    		    stmt.executeUpdate();
                }
                if(which.equals("2")){
                    System.out.print("What is the artist's new active year? ");
                    String edit = scanner.nextLine();
                    update = "update Artist set active_year = ? where Artist.Person_ID = ?;";
                    stmt = null;
                    stmt = conn.prepareStatement(update);
                    stmt.setInt(1, Integer.parseInt(edit));
                    stmt.setInt(2, Integer.parseInt(artist));
                    System.out.println("Editing ...");
	    		    stmt.executeUpdate();
                }
                if(which.equals("3")){
				    System.out.print("What is the artist's new name? ");
                    String edit1 = scanner.nextLine();
                    System.out.print("What is the artist's new active year? ");
                    String edit2 = scanner.nextLine();
                    System.out.println("Editing ...");
                    update = "update Person set Name = ? where Person.Person_ID = ?;";
                    stmt = null;
                    stmt = conn.prepareStatement(update);
                    stmt.setString(1, edit1);
                    stmt.setInt(2, Integer.parseInt(artist));
	    		    stmt.executeUpdate();

                    update = "update Artist set active_year = ? where Artist.Person_ID = ?;";
                    stmt = null;
                    stmt = conn.prepareStatement(update);
                    stmt.setInt(1, Integer.parseInt(edit2));
                    stmt.setInt(2, Integer.parseInt(artist));
                    stmt.executeUpdate();

                }
                
                
                System.out.println("***Successfully changed !***");
                String complete = 
                "Select Person.*, Artist.active_year "+
                "from Artist "+
                "left join Person on Artist.Person_ID = Person.Person_ID;";
                stmt = conn.prepareStatement(complete);
                // stmt.setInt(1, 10);
                rSet = stmt.executeQuery();
                ShowResult(rSet);
            }   
            catch(SQLException ex){
                ex.printStackTrace();

            } 

            
		}catch(Exception e) {
			System.out.println(e);
		}
		finally {
			if(rSet!=null) { rSet.close();}
			if(stmt!=null) { stmt.close();}
			// if(conn!=null) { conn.close();}

		}
		
	}  
    public static void ShowResult(ResultSet rSet){
        try{
            ResultSetMetaData rsmd = rSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) System.out.print(",  ");
            }
            System.out.print("\n");
            while(rSet.next()){
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rSet.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) System.out.print(",  ");
                }
                System.out.print("\n");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }		
	
}


	
	
