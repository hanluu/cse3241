import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Reports {
    
    public static void sqlQuery(Connection conn, String sql){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void reports(Connection conn) {
		Scanner scanner_r = new Scanner(System.in);
		while(true) {
			System.out.println("Select an action number, then input the corresponding arguments:");
			System.out.println("(1) Find the titles of all tracks by ARTIST released before YEAR.");
			System.out.println("(2) Find the total number of albums checked out by a single patron.");
			System.out.println("(3) Find the most popular actor in the database.");
			System.out.println("(4) Find the most listened to artist in the database.");
			System.out.println("(5) Find the patron who has checked out the most videos and the total number of videos they have checked out.");
			System.out.println("(6) Exit Useful Report searching.");
			String selectReport = scanner_r.nextLine();
			PreparedStatement stmt = null;
			ResultSet rSet = null;

			if(selectReport.equals("1")){
				try {
					System.out.println("Which ARTIST are you looking for?");
					String artistName = scanner_r.nextLine();
					System.out.println("What's the restriction for released year?");
					String year = scanner_r.nextLine();
					
					String sql = "SELECT Track.Track_Name " + 
						"FROM Track, Track_Album_Relationship, CreatorItemRelationship, Person, Item "	+
						"WHERE Person.Name = ? AND Item.Year < ? AND "	+				
						"Person.Person_ID = CreatorItemRelationship.Person_ID AND "	+
						"CreatorItemRelationship.Item_ID = Item.Item_ID AND " +
						"Track.Track_Name = Track_Album_Relationship.Track_Name AND "	+				
						"Item.Item_ID = Track_Album_Relationship.Item_ID";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, artistName);
					stmt.setString(2, year);
					rSet = stmt.executeQuery();
					System.out.println("\n");
					while(rSet.next()){
						String Track_Name = rSet.getString("Track_Name");
						System.out.println("Track_Name: " + Track_Name);
					}
					System.out.println(("\n=======================================\nClick to Continue"));
					String dummy  = scanner_r.nextLine();
				}
				catch(Exception ex) {
					System.out.println("Something's going wrong for Useful Report 1. " + ex.getMessage());
				}
				finally {
					if(rSet != null)	try {rSet.close();} catch(Exception ex){} finally{}
					if(stmt != null)	try {stmt.close();} catch(Exception ex){} finally{}
					//if(conn != null)	try {conn.close();} catch(Exception ex){} finally{}
				}
			}
					
			else if(selectReport.equals("2")){
				try {
					System.out.println("Which Patron are you looking for? Please input the name of the patron.");
					String Name  = scanner_r.nextLine();
					
					String sql = "SELECT count(distinct Item.Title) "	+
						"FROM  Item, Patron, LibraryCard, BorrowingHistory, Album, Person "	+
						"WHERE Person.Name = ? AND Album.Item_ID = Item.Item_ID AND Item.Item_ID = BorrowingHistory.Item_ID AND "	+
						"BorrowingHistory.Card_ID = LibraryCard.Card_ID AND LibraryCard.Patron_ID = Patron.Person_ID AND Patron.Person_ID = Person.Person_ID";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, Name);
					rSet = stmt.executeQuery();
					System.out.println("\n");
					while(rSet.next()){
						String returnCount = rSet.getString("count(distinct Item.Title)");
						System.out.println("Total Count = " + returnCount);
					}
					System.out.println(("\n=======================================\nClick to Continue"));
					String dummy  = scanner_r.nextLine();
				}
				catch(Exception ex) {
					System.out.println("Something's going wrong for Useful Report 2. " + ex.getMessage());
				}
				finally {
					if(rSet != null)	try {rSet.close();} catch(Exception ex){} finally{}
					if(stmt != null)	try {stmt.close();} catch(Exception ex){} finally{}
					//if(conn != null)	try {conn.close();} catch(Exception ex){} finally{}
				}
			}
			
			else if(selectReport.equals("3")) {
				try {
					String sql = "SELECT Name, MAX(BorrowCount) "	+					
					"FROM ( "	+					
					"SELECT Person.Name as Name, COUNT(BorrowingHistory.HistoryNo) as BorrowCount  "	+					
					"From Person, Actor, Video, Item, BorrowingHistory, CreatorItemRelationship as CIR  "	+					
					"WHERE Person.Person_ID = Actor.Person_ID AND Video.Item_ID = Item.Item_ID AND Actor.Person_ID = CIR.Person_ID  "	+					
					"AND BorrowingHistory.Item_ID = Video.Item_ID AND CIR.Item_ID = Video.Item_ID  "	+					
					"Group by Actor.Person_ID )";	
					System.out.println("\n");				
					sqlQuery(conn, sql);
					System.out.println(("\n=======================================\nClick to Continue"));
					String dummy  = scanner_r.nextLine();
				}
				catch(Exception ex) {
					System.out.println("Something's going wrong for Useful Report 3. " + ex.getMessage());
				}
				finally {
					if(rSet != null)	try {rSet.close();} catch(Exception ex){} finally{}
					if(stmt != null)	try {stmt.close();} catch(Exception ex){} finally{}
					//if(conn != null)	try {conn.close();} catch(Exception ex){} finally{}
				}
			}
			
			else if(selectReport.equals("4")) {
				try {
					String sql = "Select A.Name "	+ 
					"From( "	+	
					"Select Person.Name,Item.Title, Count(*)*Item.Length as total "	+	
					"from Item, BorrowingHistory, CreatorItemRelationship, Person "	+	
					"Where Item.Item_ID = BorrowingHistory.Item_ID and "	+	
					"Item.Item_ID in (Select Album.Item_ID from Album Where Album.Item_ID = Item.Item_ID) "	+	
					"and Item.Item_ID = CreatorItemRelationship.Item_ID and CreatorItemRelationship.Person_ID = Person.Person_ID "	+	
					"Group by Item.Item_ID "	+	
					"Order by total desc "	+	
					")A "	+	
					"limit 1; ";				
					System.out.println("\n");	
					sqlQuery(conn, sql);
					System.out.println(("\n=======================================\nClick to Continue"));
					String dummy  = scanner_r.nextLine();
				}
				catch(Exception ex) {
					System.out.println("Something's going wrong for Useful Report 4. " + ex.getMessage());
				}
				finally {
					if(rSet != null)	try {rSet.close();} catch(Exception ex){} finally{}
					if(stmt != null)	try {stmt.close();} catch(Exception ex){} finally{}
					//if(conn != null)	try {conn.close();} catch(Exception ex){} finally{}
				}
			}

			else if(selectReport.equals("5")) {
				try {
					String sql = "SELECT Person.Name, Max "	+
					"From( "	+
					"SELECT Max(Count) as Max  "	+
					"FROM(  "	+					
					"SELECT BorrowingHistory.Card_ID, Count(*) as Count  "	+					
					"FROM BorrowingHistory, Video  "	+					
					"WHERE BorrowingHistory.Item_ID = Video.Item_ID "	+					
					"Group by BorrowingHistory.Card_ID  "	+		
					")A, LibraryCard as B, Person as C " 	+					
					"WHERE A.Card_ID = B.Card_ID AND B.Patron_ID = C.Person_ID "	+					
					")E,(  "	+					
					"SELECT BorrowingHistory.Card_ID, Count(*) as Count  "	+					
					"FROM BorrowingHistory, Video  "	+					
					"WHERE BorrowingHistory.Item_ID = Video.Item_ID "	+
					"Group by BorrowingHistory.Card_ID  "	+
					")D, LibraryCard,Person "	+
					"Where E.Max = D.Count And D.Card_ID = LibraryCard.Card_ID And LibraryCard.Patron_ID = Person.Person_ID";
					System.out.println("\n");
					sqlQuery(conn, sql);
					System.out.println(("\n=======================================\nClick to Continue"));
					String dummy  = scanner_r.nextLine();
				}
				catch(Exception ex) {
					System.out.println("Something's going wrong for Useful Report 5. " + ex.getMessage());
				}
				finally {
					if(rSet != null)	try {rSet.close();} catch(Exception ex){} finally{}
					if(stmt != null)	try {stmt.close();} catch(Exception ex){} finally{}
					//if(conn != null)	try {conn.close();} catch(Exception ex){} finally{}
				}
			}
			
			else if(selectReport.equals("6")) {
				break;
			}
			/* finally best approach
			finally{
			
				/* From JSE7 onwards the try-with-resources statement is introduced. 
				* The resources in the try block will be closed automatically after the use,
				* at the end of the try block
				*  close JDBC objects
				* If not, use the following block:
			try {
				if(rs!=null) rs.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if(stmt !=null) st.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if(conn !=null) con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			}*/
		}
		scanner_r.close();
    }
}
