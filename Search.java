import java.sql.*;
import java.util.*;

public class notSearch {
	static void artist(Connection conn, Scanner scanner, String artist_name){
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sqlStatement = "SELECT Person.Name, Artist.Person_ID, Artist.Active_year FROM Person, Artist WHERE Artist.Person_ID = Person.Person_ID AND Person.Name =?";
		try{
		ps = conn.prepareStatement(sqlStatement);
		ps.setString(1,artist_name);
		rSet = ps.executeQuery();
		System.out.println("\nName\tId\tActive Year");
			while (rSet.next()){
				String name = rSet.getString("Name");
				int id = rSet.getInt("Person_ID");
				int active_year = rSet.getInt("Active_year");
				System.out.println(name+"\t"+id+"\t"+active_year);
			}
		
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

	static void track(Connection conn, Scanner scanner, String track_name){
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sqlStatement = "SELECT Track.Track_Name, Item.Title, Track.Track_No FROM Item, Album, Track, Track_Album_Relationship WHERE Album.Item_ID = Item.Item_ID AND Track_Album_Relationship.Track_name = Track.Track_Name AND Track_Album_Relationship.Item_ID = Item.Item_ID AND Track.Track_Name = ?";
		try{
		ps = conn.prepareStatement(sqlStatement);
		ps.setString(1,track_name);
		rSet = ps.executeQuery();
		System.out.println("\nName\tAlbum Name\tTrack Number");
			while (rSet.next()){
				String name = rSet.getString("Track_Name");
				String albumName = rSet.getString("Title");
				int trackNo= rSet.getInt("Track_No");
				System.out.println(name+"\t"+albumName+"\t"+trackNo);
			}
		
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
}
