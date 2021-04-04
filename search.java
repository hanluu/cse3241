import java.sql.*;

public class search {
	void artist(Connection conn, String artist_name){
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sqlStatement = "";
		try{
		ps = conn.prepareStatement(sqlStatement);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	void track(Connection conn, String track_name){
		
	}
}
