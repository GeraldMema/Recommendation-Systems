package Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Users {
	
	// Map apo ID -> pinaka [userId,itemId,oliko,epimerous1,...,epimerous7]
	Map<Integer,Collection<Integer>> dburi;
	
	// Map apo userId -> pinaka [itemId,oliko,epimerous1,...,epimerous7]
	static Map<Integer,ArrayList<Integer[]>> uri;
	
	// Map apo userId -> centroid . Opou centroid antiproswpeuei tis va8mologhseis tou xrhsth
	static Map<Integer,Double[]> centroids;
	
	public Map<Integer,Collection<Integer>> get_uri(){
		return dburi;
	}
	
	public Map<Integer,Double[]> getCentroids()
	{
		return centroids;
	}
	
	public void print_uri()
	{
		
		for(Map.Entry<Integer,ArrayList<Integer[]>> user : uri.entrySet())
		{
			System.out.println(" userId = " + user.getKey());
			
			for (Integer[] item : user.getValue()) 
			{
//				System.out.println("itemId = " + item.getValue()[0]);
				System.out.println(Arrays.toString(item));
			}
		}
		
	}
	
	/*
	 * Contructor gia thn klassh Users
	 * 
	 * apo8hkeuei ta dedomena tou pinaka ths vashs user_items_ratings sto map uri
	 * kai arxikopoiei katalhles metavlhtes
	 */
	public Users()
	{
		
		DBHandler db = new DBHandler();
		
		centroids =  new HashMap<Integer, Double[]>();
		
		db.initialize();
		
		uri = new HashMap<Integer,ArrayList<Integer[]>>();
		
		dburi =  db.getUsers_items_ratings();
		
		for (Map.Entry<Integer, Collection<Integer>> user_item_rating : dburi.entrySet()) 
		{
			
			Integer[] collection_array = new Integer[10];
			
			collection_array = user_item_rating.getValue().toArray(collection_array);
			
			int userID = collection_array[0];
			
			Integer[] rating = new Integer[9];
					
			for (int i = 1; i < 10; i++) {
				rating[i-1] = collection_array[i];
			}
			
			if( uri.containsKey(userID) )
			{
				uri.get(userID).add(rating);
			}
			else
			{
				ArrayList<Integer[]> item_rating = new ArrayList<Integer[]>();

				item_rating.add(rating);

				uri.put(userID, item_rating);
			}
			
		}
		
	}
	
	/*
	 * @return users_ids . Pinakas me ta id twn user
	 */
	public Integer[] get_users_ids()
	{
		Integer[] users_ids = new Integer[uri.size()];
		int i =0;
		for (Map.Entry<Integer,ArrayList<Integer[]>> user: uri.entrySet()) 
		{
			users_ids[i] = user.getKey();
			i++;
		}
		return users_ids;
	}
	
	/*
	 * Gyrnaei to id tou xrhsth me anazhthsh to centroid tou alliws epistrefei -1
	 */
	public static Integer getUserIdByCentroid(Double[] centroid)
	{
		for (Map.Entry<Integer, Double[]> center : centroids.entrySet()) 
		{
			if( Arrays.equals(centroid, center.getValue()) )
				return center.getKey();
		}
		
		return -1;
	}
	
	/*
	 * Epistrefei to centroid tou user me vash to id tou
	 */
	public Double[] getUserCentroid(int user)
	{
		return centroids.get(user);
	}
	
	/*
	 * Ypologizei ta centroids gia tous users kai ta apo8hkeuei sto map centroids
	 */
	public void calc_centroids()
	{
		
		for (Map.Entry<Integer, ArrayList<Integer[]>> user : uri.entrySet()) 
		{
			
			Double[] c = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			
			Integer j=0;
			Integer overal;
			Integer krithria;
			for (Integer[] item_ratings : user.getValue()) 
			{
				Double minDiafora = 5.0;
				Double maxDiafora = 0.0;
				
				for (int i = 2; i < item_ratings.length; i++) 
				{
					krithria = item_ratings[i];
					overal = item_ratings[1];
					if( krithria == -1 )
						krithria = overal;
					if(overal == -1 )
						continue;
					c[i-2] += (double)overal/(double)krithria;
					if( c[i-2] >= maxDiafora )
						maxDiafora = c[i-2];
					
					if( c[i-2] <= minDiafora )
						minDiafora = c[i-2];
					
				}
				
				c[item_ratings.length-2] = maxDiafora - minDiafora;
				
				j++;
				
			}
			
			for (int i = 0; i < c.length; i++) {
				c[i] = c[i]/j;
			}
			
			centroids.put(user.getKey(), c);
		
		}
		
	}
	
	//Epistrefei tis vathmologies xwris diplotypa
	public static int[][] PetakseIdiesGrammes(int[][] ratings)
	{
		if(ratings == null)
			return null;
		if(ratings.length == 1)
			return ratings;
		Integer PlhthosAneksarthtwnGrammwn = 0;
		Integer[] Position = new Integer[ratings.length];
		for (int i = 0; i < ratings.length; i++)
		{
			
			int flag=0;
			for (int j = i+1; j < ratings.length; j++)
			{
				double diafora = ratings[i][0] - ratings[j][0];
				for (int k = 0; k < ratings[0].length; k++)
				{	
					flag = 0;
					
					if( ratings[i][k] != (ratings[j][k] + diafora))
					{
						flag = 1;
						break;
					}
				}
				if( flag == 0)
					break;
			}
			if( flag == 1 )
			{
				Position[i] = 1;
				PlhthosAneksarthtwnGrammwn++;
				
				if( ratings[i][0]==-1)
				{
					Position[i] = 0;
					PlhthosAneksarthtwnGrammwn--;
				}
				
			}
			else
				Position[i] = 0;
			
			
		}
		int[][] finalratings = new int[PlhthosAneksarthtwnGrammwn][ratings[0].length];
		int i=0;
		int j=0;
		while(i < ratings.length)
		{
			if(Position[i] == 1){
				for (int k = 0; k < ratings[0].length; k++)
					finalratings[j][k] = ratings[i][k];
				j++;
			}
			i++;				
		}
		return finalratings;
	}
	
	//Epistrefei ton m.o twn vathm.ratings
	public double average(Integer[] item_ratings)
	{
		int j=0;
		int sum = 0;
		for (int i = 1; i < item_ratings.length; i++) {
			if(item_ratings[i] != -1)
			{
				sum += item_ratings[i];
				j++;
			}
		}
		double result = (double)sum/(double)j ;
		return result;
	}
	
	// Epistrefei ta ratings gia ton users i se pinaka int[][]
	public static int[][] get_user_ratings(Integer user)
	{
		if( !uri.containsKey(user) ){
			System.out.println("User does not exist ");
			System.out.println("size = " + uri.size());
			System.out.println("User = " + user);
			return null;
		}
		ArrayList<Integer[]> usr = uri.get(user);
		int count = 0;
		for (Integer[] item_ratings : usr) 
		{
			if( item_ratings[1] != -1 ){
				count ++;
			}
		}
		int[][] points = new int[count][8];	
		int j = 0;
		for (Integer[] item_ratings : usr) 
		{
		
			if( item_ratings[1] != -1){
				for (int i = 1; i < item_ratings.length; i++) {
					
					if( item_ratings[i] == -1 ){
						points[j][i-1] = item_ratings[1];

					}
					else
						points[j][i-1] = item_ratings[i];
				}
				j++;
			}
		}
		
		int[][] finalpoints = PetakseIdiesGrammes(points);
	
		return finalpoints;
		
	}
	
}
