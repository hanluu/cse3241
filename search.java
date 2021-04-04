import java.sql.*;
import java.util.*;

public class search {
	void artist(Connection conn, Scanner scanner, String artist_name){
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sqlStatement = "SELECT Person.Name, Actor.Person_ID, Actor.Active_year FROM Person, Actor WHERE Actor.Person_ID = Person.Person_ID AND Person.Name =?";
		try{
		ps = conn.prepareStatement(sqlStatement);
		ps.setString(1,artist_name);
		rSet = ps.executeQuery();

		
		}catch(Exception e){
			e.printStackTrace();
		}


		try{
		ps.close();
		rSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	void track(Connection conn, Scanner scanner, String track_name){
		
	}
}
