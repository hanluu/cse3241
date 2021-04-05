package osu.cse3241;

// NEW: import Date
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * <h1>CSE3241 Introduction to Database Systems - Sample Java application.</h1>
 *
 * <p>
 * Sample app to be used as guidance and a foundation for students of CSE3241
 * Introduction to Database Systems at The Ohio State University.
 * </p>
 *
 * <h2>!!! - Vulnerable to SQL injection - !!!</h2>
 * <p>
 * Correct the code so that it is not vulnerable to a SQL injection attack.
 * ("Parameter substitution" is the usual way to do this.)
 * </p>
 *
 * <p>
 * Class is written in Java SE 8 and in a procedural style. Implement a
 * constructor if you build this app out in OOP style.
 * </p>
 * <p>
 * Modify and extend this app as necessary for your project.
 * </p>
 *
 * <h2>Language Documentation:</h2>
 * <ul>
 * <li><a href="https://docs.oracle.com/javase/8/docs/">Java SE 8</a></li>
 * <li><a href="https://docs.oracle.com/javase/8/docs/api/">Java SE 8
 * API</a></li>
 * <li><a href=
 * "https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/">Java JDBC
 * API</a></li>
 * <li><a href="https://www.sqlite.org/docs.html">SQLite</a></li>
 * <li><a href="http://www.sqlitetutorial.net/sqlite-java/">SQLite Java
 * Tutorial</a></li>
 * </ul>
 *
 * <h2>MIT License</h2>
 *
 * <em>Copyright (c) 2019 Leon J. Madrid, Jeff Hachtel</em>
 *
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * </p>
 *
 *
 * @author Leon J. Madrid (madrid.1), Jeff Hachtel (hachtel.5)
 *
 */

public class CSE3241app {

	/**
	 * The database file name.
	 *
	 * Make sure the database file is in the root folder of the project if you only
	 * provide the name and extension.
	 *
	 * Otherwise, you will need to provide an absolute path from your C: drive or a
	 * relative path from the folder this class is in.
	 */
	private static String DATABASE = "Library.db";
	/**
	 * The query statement to be executed.
	 *
	 * Remember to include the semicolon at the end of the statement string. (Not
	 * all programming languages and/or packages require the semicolon (e.g.,
	 * Python's SQLite3 library))
	 */

	// line 97 - 103
	private static String sqlStatementInventory = "SELECT * FROM Inventory;";
	private static String sqlStatementItem = "SELECT * FROM Item;";
	private static String sqlStatementAudiobook = "SELECT * FROM Audiobook;";
	private static String sqlStatementArtist = "SELECT * FROM Artist;";
	private static String sqlStatementPerson = "SELECT * FROM Person;";
	private static String sqlStatementOrder = "SELECT * FROM [Order];";
	private static String sqlStatementSysUser = "SELECT * FROM SysUser;";
	private static String sqlStatementMovie = "SELECT * FROM Video;";

	/**
	 * Connects to the database if it exists, creates it if it does not, and returns
	 * the connection object.
	 *
	 * @param databaseFileName the database file name
	 * @return a connection object to the designated database
	 */
	public static Connection initializeDB(String databaseFileName) {
		/**
		 * The "Connection String" or "Connection URL".
		 *
		 * "jdbc:sqlite:" is the "subprotocol". (If this were a SQL Server database it
		 * would be "jdbc:sqlserver:".)
		 */
		String url = "jdbc:sqlite:" + databaseFileName;
		Connection conn = null; // If you create this variable inside the Try block it will be out of scope
		try {
			conn = DriverManager.getConnection(url);
			if (conn != null) {
				// Provides some positive assurance the connection and/or creation was
				// successful.
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("The connection to the database was successful.");
			} else {
				// Provides some feedback in case the connection failed but did not throw an
				// exception.
				System.out.println("Null Connection");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("There was a problem connecting to the database.");
		}
		return conn;
	}

	/**
	 * Queries the database and prints the results.
	 *
	 * @param conn a connection object
	 * @param sql  a SQL statement that returns rows This query is written with the
	 *             Statement class, tipically used for static SQL SELECT statements
	 */
	public static void sqlQuery(Connection conn, String sql) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String value = rsmd.getColumnName(i);
				System.out.print(value);
				if (i < columnCount) {
					System.out.print(",  ");
				}
			}
			System.out.print("\n");
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
					if (i < columnCount) {
						System.out.print(",  ");
					}
				}
				System.out.print("\n");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Line 177 til END

	public static void insertPerson(Connection conn, int person_ID, String name) throws SQLException {
		String sql = "INSERT INTO Person(Person_ID, Name) VALUES (?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, person_ID);
			stmt.setString(2, name);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertArtist(Connection conn, int person_ID, int active_year) throws SQLException {
		String sql = "INSERT INTO Artist(Person_ID, Active_year) VALUES (?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, person_ID);
			stmt.setInt(2, active_year);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertInventory(Connection conn, int inventory_ID, String category, String status)
			throws SQLException {
		String sql = "INSERT INTO Inventory(Inventory_ID, Category, Status) VALUES (?,?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, inventory_ID);
			stmt.setString(2, category);
			stmt.setString(3, status);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertItem(Connection conn, int item_ID, int inventory_ID, int length, String content_rate,
			int year, String title, int num_copy) throws SQLException {
		String sql = "INSERT INTO Item(Item_ID, Inventory_ID, Length, Content_Rating, Year, Title, Num_Copies) VALUES (?,?,?,?,?,?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, item_ID);
			stmt.setInt(2, inventory_ID);
			stmt.setInt(3, length);
			stmt.setString(4, content_rate);
			stmt.setInt(5, year);
			stmt.setString(6, title);
			stmt.setInt(7, num_copy);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertAudiobook(Connection conn, int item_ID, String genre, int chapter) throws SQLException {
		String sql = "INSERT INTO Audiobook(Item_ID, genre, chapter) VALUES (?,?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, item_ID);
			stmt.setString(2, genre);
			stmt.setInt(3, chapter);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertOrder(Connection conn, int sysUser_ID, int order_ID, double price, Date arrive_date,
			boolean isLate, int no_copies, int item_ID) throws SQLException {
		String sql = "INSERT INTO [Order](SysUser_ID, Order_ID, Price, Arrive_date, Arrive_late, No_copies, Item_ID) VALUES (?,?,?,?,?,?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, sysUser_ID);
			stmt.setInt(2, order_ID);
			stmt.setDouble(3, price);
			stmt.setDate(4, arrive_date);
			stmt.setBoolean(5, isLate);
			stmt.setInt(6, no_copies);
			stmt.setInt(7, item_ID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void insertVideo(Connection conn, int item_ID, String genre) throws SQLException {
		String sql = "INSERT INTO Video(Item_ID, genre) VALUES (?,?);";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, item_ID);
			stmt.setString(2, genre);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void updateItem(Connection conn, int item_ID, int num_copy) throws SQLException {
		String sql = "UPDATE Item SET Num_Copies = ? WHERE Item.Item_ID = ?;";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(2, item_ID);
			stmt.setInt(1, num_copy);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public static void main(String[] args) {
		Connection conn = initializeDB(DATABASE);
		sqlQuery(conn, sqlStatementSysUser);
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your SysUser_ID.");
		int sysUser_ID = input.nextInt();
		input.nextLine();
		System.out.println("*********************************************************************");

		sqlQuery(conn, sqlStatementItem);
		System.out.println("1/ Order a new movie.");
		System.out.println("2/ Add more to an existing movie.");
		System.out.println("Enter 1 or 2 to choose");
		int choice = input.nextInt();

		System.out.println("1. What is item_ID?");
		int item_ID = input.nextInt();
		input.nextLine();
		System.out.println("2. What is the name of the movie?");
		String title = input.nextLine();
		System.out.println("3. What is the length of the movie?");
		int length = input.nextInt();
		input.nextLine();
		System.out.println("4. What is the content rating of the movie?");
		String content_rate = input.nextLine();
		System.out.println("5. What is the release year of the movie?");
		int year = input.nextInt();
		input.nextLine();
		System.out.println("6. What is the inventory ID?");
		int inventory_ID = input.nextInt();
		input.nextLine();
		System.out.println("7. How many copies will you order?");
		int num_copies = input.nextInt();
		input.nextLine();
		System.out.println("*********************************************************************");

		sqlQuery(conn, sqlStatementOrder);
		System.out.println("8. What is the Order ID?");
		int order_ID = input.nextInt();
		input.nextLine();
		System.out.println("9. How much total?");
		double price = input.nextDouble();
		input.nextLine();
		System.out.println("10. What is the estimated arrival date? (yyyy-mm-dd)");
		String date = input.nextLine();
		Date arrival_date = Date.valueOf(date);
		System.out.println("11. Is it late or not? Y for Yes and N for No");
		String late = input.nextLine();
		boolean isLate = false;
		if (late.compareToIgnoreCase("Y") == 0) {
			isLate = true;
		}
		System.out.println("*********************************************************************");

		sqlQuery(conn, sqlStatementMovie);
		System.out.println("12. What is the genre?");
		String genre = input.nextLine();
		if (choice == 1) {
			try {
				insertOrder(conn, sysUser_ID, order_ID, price, arrival_date, isLate, num_copies, item_ID);
				System.out.println("*********************************************************************");
				System.out.println("Completed adding a new order.");
				insertItem(conn, item_ID, inventory_ID, length, content_rate, year, title, num_copies);
				insertVideo(conn, item_ID, genre);
				System.out.println("*********************************************************************");
				System.out.println("Completed.");
				System.out.println("*********************************************************************");
				System.out.println("Updated data.");
				sqlQuery(conn, sqlStatementOrder);
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		} else if (choice == 2) {
			try {
				insertOrder(conn, sysUser_ID, order_ID, price, arrival_date, isLate, num_copies, item_ID);
				System.out.println("*********************************************************************");
				System.out.println("Completed adding a new order.");
				String sql = "SELECT Num_copies FROM Item WHERE Item.Item_ID = (?);";
				PreparedStatement stmt = null;
				ResultSet rs = null;
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, item_ID);
				rs = stmt.executeQuery();
				int num_cp = 0;
				while (rs.next()) {
					num_cp = rs.getInt("Num_copies");
				}
				// update the number of copies in item by adding no_copies from the new order
				num_copies = num_cp + num_copies;
				updateItem(conn, item_ID, num_copies);
				System.out.println("*********************************************************************");
				System.out.println("Completed.");
				System.out.println("*********************************************************************");
				System.out.println("Updated data.");
				// sqlQuery(conn, sqlStatementOrder);
				sqlQuery(conn, sqlStatementItem);
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		} else {
			System.out.println("Only enter either 1 or 2.");
		}

		/*
		 * finally best approach finally{
		 *
		 * /* From JSE7 onwards the try-with-resources statement is introduced. The
		 * resources in the try block will be closed automatically after the use, at the
		 * end of the try block close JDBC objects If not, use the following block: try
		 * { if(rs!=null) rs.close(); } catch (SQLException se) { se.printStackTrace();
		 * } try { if(stmt !=null) st.close(); } catch (SQLException se) {
		 * se.printStackTrace(); } try { if(conn !=null) con.close(); } catch
		 * (SQLException se) { se.printStackTrace(); } }
		 */
		input.close();
	}
}

