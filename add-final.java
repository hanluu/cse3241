package osu.cse3241;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
			stmt.setString(2, content_rate);
			stmt.setInt(5, year);
			stmt.setString(3, title);
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

	public static void main(String[] args) {
		System.out.println("1. Insert a new audiobook into the database");
		System.out.println("2. Insert a new artist into the database");
		System.out.println("Choose between the two options for adding a new data into the database. Enter 1 or 2.");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		Connection conn = initializeDB(DATABASE);
		if (choice == 1) {
			sqlQuery(conn, sqlStatementInventory);
			System.out.println("*********************************************************************");
			sqlQuery(conn, sqlStatementItem);
			System.out.println("*********************************************************************");
			sqlQuery(conn, sqlStatementAudiobook);
			System.out.println("*********************************************************************");
			System.out.println("1. Insert a new data into Item table.");
			System.out.println("Inventory ID: ");
			int inventory_ID = input.nextInt();
			System.out.println("Item ID: ");
			int item_ID = input.nextInt();
			System.out.println("Length: ");
			int length = input.nextInt();
			System.out.println("Content_Rating: ");
			input.nextLine();
			String content_rate = input.nextLine();
			System.out.println("Year: ");
			int year = input.nextInt();
			System.out.println("Title: ");
			input.nextLine();
			String title = input.nextLine();
			System.out.println("Number of copies: ");
			int num_copy = input.nextInt();

			System.out.println("2. Insert a new data into Audiobook table.");
			System.out.println("Genre: ");
			input.nextLine();
			String genre = input.nextLine();
			System.out.println("Chapter: ");
			int chapter = input.nextInt();

			try {
				insertItem(conn, item_ID, inventory_ID, length, content_rate, year, title, num_copy);
				insertAudiobook(conn, item_ID, genre, chapter);
				System.out.println("*********************************************************************");
				System.out.println("Completed.");
				System.out.println("*********************************************************************");
				System.out.println("Updated data.");
				sqlQuery(conn, sqlStatementAudiobook);
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		} else if (choice == 2) {
			sqlQuery(conn, sqlStatementArtist);
			System.out.println("*********************************************************************");
			sqlQuery(conn, sqlStatementPerson);
			System.out.println("*********************************************************************");
			System.out.println("1. Insert a new data into Person table. Need person_ID and name");
			System.out.println("Person ID: ");
			int person_ID = input.nextInt();
			System.out.println("Name: ");
			input.nextLine();
			String name = input.nextLine();
			try {
				insertPerson(conn, person_ID, name);
				System.out.println("*********************************************************************");
				System.out.println("Insert a new data into Artist table. Need the year that artist is active in xxxx");
				System.out.println("Active year: ");
				int active_year = input.nextInt();
				insertArtist(conn, person_ID, active_year);
				System.out.println("*********************************************************************");
				System.out.println("Completed.");
				System.out.println("*********************************************************************");
				System.out.println("Updated data.");
				sqlQuery(conn, sqlStatementArtist);
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

