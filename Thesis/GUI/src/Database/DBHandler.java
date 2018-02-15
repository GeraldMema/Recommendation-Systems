package Database;

import java.sql.*;
import java.util.*;

public class DBHandler {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/trip_advisor_hotels";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "3110";
	
	Map<Integer,String> users;
	Map<Integer,String> items;
	Map<Integer,Collection<Integer>> meta;
	Map<Integer,Collection<Integer>> users_items_ratings;
	Connection conn;
	Statement stmt;
	
	public Map<Integer, String> getUsers() {
		return users;
	}
	public Map<Integer, String> getItems() {
		return items;
	}

	public Map<Integer, Collection<Integer>> getMeta() {
		return meta;
	}

	public Map<Integer, Collection<Integer>> getUsers_items_ratings() {
		return users_items_ratings;
	}
	
	public void printUsers()
	{
		
		for(Map.Entry<Integer,String> user : users.entrySet()){

			//Display values
			System.out.print("ID: " + user.getKey());
			System.out.println(", UserName: " + user.getValue());
		}
		
	}
	
	public void printItems()
	{
		
		for(Map.Entry<Integer,String> item : items.entrySet()){

			//Display values
			System.out.print("ID: " + item.getKey());
			System.out.println(", ItemName: " + item.getValue());
		}
		
	}
	
	public void printMeta()
	{
		
		for(Map.Entry<Integer,Collection<Integer>> m : meta.entrySet()){

			//Display values
			System.out.println("ID: " + m.getKey());
			Collection<Integer> a = m.getValue();
			System.out.println("number of Users : " + a.toArray()[0]);
			System.out.println("number of Items : " + a.toArray()[1]);
			System.out.println("number of Ratings including Multicritiria and Overall : " + a.toArray()[2]);
			System.out.println("max Rating : " + a.toArray()[3]);
		}
		
	}
	
	public void printUsers_Items_Ratings()
	{
		
		System.out.println("ID userID itemID rating0 rating1 rating2 rating3 rating4 rating5 rating6 rating7");
		
		for(Map.Entry<Integer,Collection<Integer>> m : users_items_ratings.entrySet()){

			//Display values
			System.out.print(m.getKey());
			Collection<Integer> a = m.getValue();
			System.out.print(" " + a.toArray()[0]);
			System.out.print(" " + a.toArray()[1]);
			System.out.print(" " + a.toArray()[2]);
			System.out.print(" " + a.toArray()[3]);
			System.out.print(" " + a.toArray()[4]);
			System.out.print(" " + a.toArray()[5]);
			System.out.print(" " + a.toArray()[6]);
			System.out.print(" " + a.toArray()[7]);
			System.out.print(" " + a.toArray()[8]);
			System.out.println(" " + a.toArray()[9]);
			
		}
		
	}
	
	public DBHandler(){
		
		users = new HashMap<Integer,String>();
		items = new HashMap<Integer,String>();
		meta = new HashMap<Integer,Collection<Integer>>();
		users_items_ratings = new HashMap<Integer,Collection<Integer>>();
		
		
	}
	
	private void register_JDBC_driver(){
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		}
		catch(Exception se)
		{
			se.printStackTrace();
		}
	}
	
	private void open_a_connection(){
		
		System.out.println("Connecting to database...");
		try{

			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception se)
		{
			se.printStackTrace();
		}
		
	}
	
	private void setDatabase()
	{
		try
		{
			
			//get Users
			
//			System.out.println("Setting users...");
			stmt = conn.createStatement();
			String sql;
//			sql = "SELECT id, UserName FROM users";
			ResultSet rs ;
//			rs = stmt.executeQuery(sql);

//			while(rs.next()){
//				//Retrieve by column name
//				int id  = rs.getInt("id");
//				String username = rs.getString("UserName");
//
//				users.put(id,username);
//			}
//			rs.close();
			
			//get Items
			
//			System.out.println("Setting items...");
//			sql = "SELECT id, ItemName FROM items";
//			rs = stmt.executeQuery(sql);
//
//			while(rs.next()){
//
//				int id  = rs.getInt("id");
//				String username = rs.getString("ItemName");
//
//				items.put(id,username);
//			}
//			rs.close();
			
			// get meta_data
			
			System.out.println("Setting meta...");
			sql = "SELECT * FROM meta_data";
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				
				int id  = rs.getInt("Id");
				int number_Users = rs.getInt("number_Users");
				int number_Items = rs.getInt("number_Items");
				int number_Ratings = rs.getInt("number_Ratings");
				int max_Rating = rs.getInt("Max_Rating");

				Collection<Integer> coll = new ArrayList<Integer>();
				coll.add(number_Users);
				coll.add(number_Items);
				coll.add(number_Ratings);
				coll.add(max_Rating);
				
				meta.put(id, coll);
				
			}
			rs.close();
			
			// get user_items_ratings

			System.out.println("Setting users_items_ratings...");
			sql = "SELECT * FROM users_items_ratings";
			rs = stmt.executeQuery(sql);

			while(rs.next()){

				int id  = rs.getInt("ID");
				int userid = rs.getInt("UserID");
				int itemid = rs.getInt("ItemID");
				int rating0 = rs.getInt("Rating0");
				int rating1 = rs.getInt("Rating1");
				int rating2 = rs.getInt("Rating2");
				int rating3 = rs.getInt("Rating3");
				int rating4 = rs.getInt("Rating4");
				int rating5 = rs.getInt("Rating5");
				int rating6 = rs.getInt("Rating6");
				int rating7 = rs.getInt("Rating7");
				

				Collection<Integer> coll = new ArrayList<Integer>();
				coll.add(userid);
				coll.add(itemid);
				coll.add(rating0);
				coll.add(rating1);
				coll.add(rating2);
				coll.add(rating3);
				coll.add(rating4);
				coll.add(rating5);
				coll.add(rating6);
				coll.add(rating7);
				
				users_items_ratings.put(id, coll);

			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch(SQLException se)
		{
		      //Handle errors for JDBC
		      se.printStackTrace();
		}
		catch(Exception e)
		{
			//Handle errors for Class.forName
			e.printStackTrace();	
		}
		finally
		{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
		
	}
	
	public void initialize(){
		
		register_JDBC_driver();
		
		open_a_connection();
		
		setDatabase();
		
	}

}
