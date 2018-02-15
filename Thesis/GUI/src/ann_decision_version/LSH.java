package ann_decision_version;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Random;
import ann_decision_version.Norm;

import Database.Users;

public class LSH {
	
	/* map hash function-> ( kados-> Points sto kado) */
	Map<Double[],Hashtable<Long,ArrayList<int[]>>> map;
	
	/* map hash function-> ( kados-> Points sto kado) opou twra to kleidi sto kado einai typou Long kai to arraylist einai Double[]*/
	Map<Double[],Hashtable<Double,ArrayList<Double[]>>> d_map;
	
	/* map pou diaxeirizetai thn lsh leitourgia gia ta coefficients */
	Map<Double[],Hashtable<Double,Map<Integer,double[]>>> coefs;
	
	/* Map pou diathrei tous geitones enos xrhsth */
	Map <Double,Integer> listOfNeighbours;
	
	/*Map apo pollaplasiasth apotelesmatos hashFunction-> HashTable<kleidi,Map<IdUser,overallMultipliedByCoef>*/
	Map<Double,Hashtable<Double,Map<Integer,Double>>> overalls;
	
	public static Map<Double[],Integer> centroids;
	
	public Map<Double[],Integer> getCentroids()
	{
		return centroids;
	}
	
	public Double[] getCentroid(Integer userId){
		
		for (Map.Entry<Double[], Integer> centroid : centroids.entrySet()) {
			if(userId == centroid.getValue() )
				return centroid.getKey();
		}
		
		return null;
	}
	
	/*
	 * return map apo indexUser->IndexGeitonwn
	 */
//	public Map<Integer,int[]> getNeighbourOverall()
//	{
//		
//		int indexOfNeighbours[];
//		Map<Integer,int[]> map = new HashMap<Integer,int[]>();
//		
//		for (Map.Entry<Double,Hashtable<Double,Map<Integer,Double>>> hashTable : overalls.entrySet()) 
//		{
//			for (Map.Entry<Double,Map<Integer,Double>> kados : hashTable.getValue().entrySet()) 
//			{
//				Map<Integer,Double> tempKados = kados.getValue();
//				
//				for (Integer indexOfNeighbour : tempKados.keySet()) 
//				{
//					int i = 0;
//					
//					indexOfNeighbours = new int[Math.max(1,tempKados.size()-1)];
//					indexOfNeighbours[0] = -1;
//					
//					for (Integer indexOfNeighbour2 : tempKados.keySet()) 
//					{
//						if(indexOfNeighbour2 != indexOfNeighbour)
//						{
//							indexOfNeighbours[i] = indexOfNeighbour2;
//							i++;
//						}
//					}
//					
//					map.put(indexOfNeighbour, indexOfNeighbours);
//				}
//					
//			}
//			
//		}
//		
//		return map;
//	}
	
	/*
	 * @param overallMultipliedByCoef einai to overall tou xrhsth pollaplasiasmeno me to coef tou
	 * @param neighbourI o deikths i tou user pou 8eloume na valoume sta hashtable
	 * 
	 * Pros8etei sta hash table gia to overall to overallMultipliedByCoef
	 */
	public void addOverall(double overallMultipliedByCoef,Integer neighbourI)
	{
		double key = 0;
		
		for (Map.Entry<Double,Hashtable<Double,Map<Integer,Double>>> hashTable : overalls.entrySet()) 
		{
			key = hashTable.getKey()*getHashValueForOverall(overallMultipliedByCoef);
			
			if(hashTable.getValue().containsKey(key))
			{
				hashTable.getValue().get(key).put(neighbourI,overallMultipliedByCoef);
			}
			else
			{
				Map<Integer,Double> map = new HashMap<Integer,Double>();
				map.put(neighbourI,overallMultipliedByCoef);
				hashTable.getValue().put(key, map);
			}
			
		}
	}
	
	/*
	 * Ektypwnei ta hashtable pou exoun kathgoriopoihmena ta overall
	 */
	public void printOveralls()
	{
		System.out.println("Printing overalls...");
		for (Map.Entry<Double,Hashtable<Double,Map<Integer,Double>>> hashTable : overalls.entrySet()) 
		{
			System.out.println("\nHashTable:");
			for(Map.Entry<Double,Map<Integer,Double>> entry : hashTable.getValue().entrySet())
			{
				System.out.println("key = " + entry.getKey() );
				
				for (Map.Entry<Integer,Double> eggrafh : entry.getValue().entrySet()) {
					System.out.println("User : " + eggrafh.getKey() + " value = " + eggrafh.getValue());
				}
				
			}
		}
	}
	
	/*
	 * @param number_of_hash_tables to plh8os twn hash table pou 8eloume na exoume gia na vroume geitones
	 * 
	 * desmeuei mnhmh gia ta hash table twn overall
	 */
	public void initializeOveralls(int number_of_hash_tables)
	{
		overalls = new HashMap<Double,Hashtable<Double,Map<Integer,Double>>>();
		
		Random r = new Random();
		
		for (int i = 0; i < number_of_hash_tables; i++) 
		{
			Integer random = r.nextInt(number_of_hash_tables*10) + 1;
			
			Double randomMultiplier = random * 1.0;
			
			Hashtable<Double,Map<Integer,Double>> hash_table = new Hashtable<Double,Map<Integer,Double>>();

			overalls.put(randomMultiplier,hash_table);

		}
	}
	
	/*
	 * Paragei ena tyxaia arxikopoihmeno 7diastato dianysma me to opoio
	 * pollaplasiazontai ta shmeia tou HashTable gia na mpoune se kadous
	 * 
	 * @return vector tyxaia arxikopoihmeno 7diastato dianysma me katanomh gauss kai a8roisma stoixeiwn = 1
	 */
	private Double[] generate_random_unit_vector()
	{
		
		Double [] vector = new Double[7];
		Random r = new Random();
		double mag = 0;
		
		for(int i = 0; i < 7; i++)
		{
			vector[i] = r.nextGaussian();
			mag += vector[i]*vector[i];
		}
		
		mag = Math.sqrt(mag);
		
		for(int i = 0; i < 7; i++)
		{
			vector[i] = vector[i] / mag ;
		}

		return vector;
		
	}
	
	private Double[] generate_random_unit_vector(int dimentions)
	{
		
		Double [] vector = new Double[dimentions];
		Random r = new Random();
		double mag = 0;
		
		for(int i = 0; i < dimentions; i++)
		{
			vector[i] = r.nextGaussian();
			mag += vector[i]*vector[i];
		}
		
		mag = Math.sqrt(mag);
		
		for(int i = 0; i < dimentions; i++)
		{
			vector[i] = vector[i] / mag ;
		}

		return vector;
		
	}

	/*
	 * Provalei to point sto vector kai gyrnaei to a8roisma pou xrhsimopoieitai gia kleidi
	 * tou kadou sto opoio mpainei to shmeio
	 * 
	 * @param point . 7diastato shmeio pou vazoume sto HashTable
	 * 
	 * @param vector . monadiko vector tou HashTable pou paragetai tyxaia
	 * to opoio voh8aei sth paragwgh toy hash value tou point
	 * 
	 * @return sum . Einai to hash value tou point
	 */
	private double project_point_to_random_vector(Double[] point , Double[] vector)
	{
		
		double sum = 0;
		
		for (int i = 0; i < 7; i++) 
		{
			sum += point[i]*vector[i];
		}
		
		return sum ;
		
	}
	

	/*
	 * Provalei to point sto vector kai gyrnaei to a8roisma pou xrhsimopoieitai gia kleidi
	 * tou kadou sto opoio mpainei to shmeio
	 * 
	 * @param point . 7diastato shmeio pou vazoume sto HashTable
	 * 
	 * @param vector . monadiko vector tou HashTable pou paragetai tyxaia
	 * to opoio voh8aei sth paragwgh toy hash value tou point
	 * 
	 * @dimention . H diastash tou shmeioy kai toy vector
	 * 
	 * @return sum . Einai to hash value tou point
	 */
	private double project_point_to_random_vector(double[] point , Double[] vector, int dimention)
	{
		
		double sum = 0;
		
		for (int i = 0; i < dimention; i++) 
		{
			sum += point[i]*vector[i];
		}
		
		return sum ;
		
	}
	
	private double project_point_to_random_vector(int[] point , Double[] vector, int dimention)
	{
		
		double sum = 0;
		
		for (int i = 0; i < dimention; i++) 
		{
			sum += point[i]*vector[i];
		}
		
		return sum ;
		
	}
	
	/*
	 * Pros8etei (enwnei) sto telos tou pinaka twn va8mologiwn tou xrhsh tis va8mologiesGeitona 
	 * 
	 * @param va8mologiesGeitona . Pinakas me va8mologies geitona me grammes tou typou [oliko,epimerous1,..,epimerous7]
	 * @param va8mologiesXrhsth . Pinakas me va8mologies xrhsth me grammes tou typou [oliko,epimerous1,..,epimerous7]
	 */
	public int[][] getTempPoints(int[][] va8mologiesGeitona,int[][] va8mologiesXrhsth)
	{
		
		int[][] TempPoints = new int[va8mologiesGeitona.length+va8mologiesXrhsth.length][va8mologiesGeitona[0].length];
		for (int j = 0; j < va8mologiesXrhsth.length; j++) {
			System.arraycopy(va8mologiesXrhsth[j],0 , TempPoints[j] , 0, va8mologiesGeitona[0].length);
		}
		for (int j = 0; j < va8mologiesGeitona.length; j++) {
			System.arraycopy(va8mologiesGeitona[j],0 , TempPoints[j+va8mologiesXrhsth.length] , 0, va8mologiesGeitona[0].length);
		}		
		return TempPoints;
	}

	/*
	 * Vriskei to id tou kontinoterou sto xrhth geitona kai ftiaxnei to listOfNeighbours(map apo apostaseis xrhsth me geitones)
	 * 
	 * @param centroid . Antiprosopeutiko 7diastato shmeio tou xrhsth pou den exei arketes grammes
	 * 
	 * @return retId . Id tou kontinoterou geitona . An den yparxei autos gyrnaei -1
	 */
	private Integer findClosestNeighbour(Double[] centroid)
	{
		Double min_distance = 100000000.0; // arxikopoioume thn apostasth tou kontinoterou geitona se ena poly megalo ari8mo.
		Integer neighbourId = -1 ; // krataei to id tou kontinoterou geitona
		Integer retId = -1;
		Double[] retNeighbour = new Double[centroid.length];
		
		// gia ka8e hashTable
		for(Map.Entry<Double[],Hashtable<Double ,ArrayList<Double[]>>> hashtable : d_map.entrySet())
		{

			Double kadosKey = getHashValue(centroid, hashtable.getKey());
			
			// vres to kontinotero geitona sto xrhsth mas
			for (Double[] neighbour : hashtable.getValue().get(kadosKey) ) 
			{
				// ean to centroid den einai tou xrhsth mas dhladh einai kapoiou geitona
				if( !Arrays.equals(centroid, neighbour) ) 
				{

					neighbourId = centroids.get(neighbour);

					Double distance = Norm.get_norm(centroid,neighbour);
					listOfNeighbours.put(distance, neighbourId);

					//dwse mou xrhsth pou einai o kontinoteros geitonas kai exei arketa items na dwsei
					if ( Double.compare(distance, min_distance)  < 0)
					{
						min_distance = distance;
						retId = neighbourId;
						System.arraycopy(neighbour, 0, retNeighbour, 0, neighbour.length);
					}

				}

			}
		}
		return retId;
	}
	
	/*
	 * Pros8etei arketes grammes stis va8mologies tou xrhsth prokeimenou na mporoume na
	 * efarmosoume grammikh palindromish
	 * 
	 * @param userId . Id tou user pou den xei arketes grammes
	 * 
	 * @param retId . Id tou kontinoterou geitona
	 * 
	 * @param va8mologiesXrhsth . Pinakas va8mologiewn tou xrhsth pou den exei va8mologhsei
	 * arketa antikeimena
	 * 
	 * @return ftiagmenosPinakas . O pinakas em va8mologies tou xrhsth ston opoiwn exoun proste8ei
	 * arketes grammes gia na efarmostei h grammikh palindromish
	 * 
	 */
	private int[][] addRatings(Integer userId,Integer retId,int[][] va8mologiesXrhsth)
	{
		int[][] ftiagmenosPinakas = null;
		
		// sortaroume tis apostaseis
		Map<Double,Integer> sortDist = new TreeMap<Double,Integer>(listOfNeighbours);

		//ftiaxnoume enan iterator
		Iterator<Entry<Double, Integer>> nextId = sortDist.entrySet().iterator();

		// kai ena entry
		Map.Entry<Double,Integer> entry = null;

		//pros8etoume sto telos twn va8mologiwn tou xrhsth tis va8mologies tou kontinoterou geitona
		int[][] neesVa8mologiesXrhsth = getTempPoints(Users.get_user_ratings(retId),va8mologiesXrhsth);
		
		// afairoume merikes grammes pou mhdenizoun to apotelesma ths grammikhs palindromishs
		ftiagmenosPinakas = Users.PetakseIdiesGrammes(neesVa8mologiesXrhsth);

		entry = nextId.next(); // ton elaxisto ton exoume elegksei epanw , opote paw ston amesws kontinotero
		
		while(ftiagmenosPinakas.length < 1.5* ftiagmenosPinakas[0].length && nextId.hasNext())
		{
			entry = nextId.next();

			int nId = (Integer)entry.getValue();
			neesVa8mologiesXrhsth = getTempPoints(Users.get_user_ratings(nId),ftiagmenosPinakas);
			ftiagmenosPinakas = Users.PetakseIdiesGrammes(neesVa8mologiesXrhsth);

		}
		
		return ftiagmenosPinakas;
		
	}
	
	/*
	 * Vriskei to kontinotero geitona tou xrhsth pou exei arketa items
	 * etsi wste na mporoume na ta pros8esoume sto xrhsth kai to plh8os
	 * tou a8roismatos na einai megalytero tou plh8ous twn epimerous krhthriwn
	 * me vash tis apostaseis sta centroids
	 * 
	 * @param centroid . Shmeio pou 8eloume na valoume sto HashTable
	 * 
	 * @param va8mologiesXrhsth . Oi va8mologies pou exei dwsei o xrhsths gia ton opoio psaxnoume kontinotero geitona
	 * . O pinakas einai ths morfhs [oliko, epimerous1,...,epimerous7]
	 * 
	 * @param userId . To id tou xrhsth o opoios den exei va8mologhsei arketa antikeimena
	 * 
	 * @return va8mologiesXrhsth . O pinakas twn va8mologiwn tou xrhsth ston opoio exoun proste8ei twra
	 * arketes grammes gia na ginei h grammikh palindromish
	 * 
	 */
	public int[][] getRatingsFromNeighbour(Double[] centroid,int[][] va8mologiesXrhsth,Integer userId)
	{
		// ean oi va8molgies tou xrhsth periexoun arketers grammes ( 8 ) prokeimenou na efarmostei
		// h grammikh palindromish tote teleiwnei h anadromh
		if( va8mologiesXrhsth.length >=( 1.5* va8mologiesXrhsth[0].length))
			return va8mologiesXrhsth;
		
		listOfNeighbours = new HashMap<Double,Integer>();
		
		Integer retId = findClosestNeighbour(centroid);

		if( retId != -1 ) // dhladh yparxei kontinoteros geitonas
		{
			
			int[][] ftiagmenosPinakas = addRatings(userId, retId, va8mologiesXrhsth);
			
			return ftiagmenosPinakas;
			
		}
		else 
		{
			System.out.println("NULL");
			return va8mologiesXrhsth;
		}
	}
	
	/*
	 * Ektypwnei ta hashTables kai ta epriexomena tous
	 */
	public void print_map()
	{
		System.out.println("Printing map...");
		for(Map.Entry<Double[],Hashtable<Long,ArrayList<int[]>>> hashtable : map.entrySet())
		{
			System.out.println("u = " + Arrays.toString(hashtable.getKey()));
			
			for(Map.Entry<Long, ArrayList<int[]>> entry : hashtable.getValue().entrySet())
			{
				System.out.println("key = " + entry.getKey() );
				
				int[] initialpoint = new int[7];
				int sum_dist = 0;
				int i = 0;
				for (int[] point : entry.getValue()) {
					
					if( i != 0)
						sum_dist += Norm.get_norm(initialpoint, point);
					System.out.println(" point = " + Arrays.toString(point));
					initialpoint = point;
					i++;
				}
				System.out.println("with sum_dist = "+ sum_dist);
			}
		}
	}
	
	/*
	 * Ektypwnei to hashMap me shmeia pou einai typou Double[]
	 */
	public void print_d_map()
	{
		System.out.println("Printing d_map...");
		for(Map.Entry<Double[],Hashtable<Double,ArrayList<Double[]>>> hashtable : d_map.entrySet())
		{
			System.out.println("u = " + Arrays.toString(hashtable.getKey()));
			
			for(Map.Entry<Double, ArrayList<Double[]>> entry : hashtable.getValue().entrySet())
			{
				System.out.println("key = " + entry.getKey() );
				
				Double[] initialpoint = new Double[7];
				double sum_dist = 0;
				int i = 0;
				for (Double[] point : entry.getValue()) {
					
					if( i != 0)
						sum_dist += Norm.get_norm(initialpoint, point);
					System.out.println(" point = " + Arrays.toString(point));
					initialpoint = point;
					i++;
				}
				System.out.println("with sum_dist = "+ sum_dist);
			}
		}
	}
	
	/*
	 * Ypologizei to hashValue tou point sto HashTable
	 * 
	 * @return result . H hashValue tou point.
	 */
	private double getHashValue(Double[] point, Double[] vector)
	{
		double result = project_point_to_random_vector(point, vector);
		
		result = result - (int)result;
		return Math.round(result * 1e1) / 1e1;
	}
	
	private double getHashValue(double[] point, Double[] vector,int dimention)
	{
		double result = project_point_to_random_vector(point, vector,dimention);
		
		result = result - (int)result;
		return Math.round(result * 1e2) / 1e2;
	}
	
	private double getHashValueForOverall(double value)
	{
		value = Math.round(value * 1e2) / 1e2;
		double result;
		result = value/500;
		result = Math.round(result *1e2)/ 1e2;
		return result;
	}
	
	/*
	 * Topo8etei sto katalhlo kado to antiprosopeutiko gia ka8e xrhsth shmeio se ola ta hashTables
	 * 
	 * @param point . To point pou 8elw na proste8ei
	 */
	public void add_point(Double[] point)
	{
		double result = 0;
		
		for (Map.Entry<Double[],Hashtable<Double,ArrayList<Double[]>>> entry : d_map.entrySet()) 
		{
			
			result = getHashValue(point, entry.getKey());
			
			if(entry.getValue().containsKey(result))
			{
				entry.getValue().get(result).add(point);
			}
			else
			{
				ArrayList<Double[]> a  = new ArrayList<Double[]>();
				a.add(point);
				entry.getValue().put(result, a);
			}
			
		}
		
	}
	
	/*
	 * Afairei to point apo ola ta hashTables
	 * 
	 * @param point . To point pou 8elw na afaire8ei
	 */
	public void remove_point(Double[] point)
	{
		double result = 0;
		
		for (Map.Entry<Double[],Hashtable<Double,ArrayList<Double[]>>> entry : d_map.entrySet()) 
		{
			
			result = project_point_to_random_vector(point, entry.getKey());
			
			result = getHashValue(point, entry.getKey());
			
			if(entry.getValue().containsKey(result))
			{
				entry.getValue().get(result).remove(point);
			}

		}
		
	}
	
	public List<double[]> getNeighboursByCoefsById( Map<double[],Integer> userIdToCoef , Integer userId,double[] userCoef)
	{
		List<double[]> retVar = new ArrayList<double[]>();
		
		for(Map.Entry<Double[],Hashtable<Double,Map<Integer,double[]>>> hashtable : coefs.entrySet())
		{
			Double kados = getHashValue(userCoef, hashtable.getKey(),hashtable.getKey().length);
				
			for (Map.Entry<Integer,double[]> entry : hashtable.getValue().get(kados).entrySet()) {

				if( retVar.contains(entry.getValue()) || entry.getKey() == userId )
					continue;

				retVar.add(entry.getValue());

			}
				
		}
		
		return retVar;
	}
	
	public void printCoefs()
	{
		
		for (Map.Entry<Double[],Hashtable<Double,Map<Integer,double[]>>> hashTable : coefs.entrySet()) 
		{
			System.out.println("HashTable :");
			for (Map.Entry<Double,Map<Integer,double[]>> kados : hashTable.getValue().entrySet()) 
			{
				System.out.println("Key = " + kados.getKey());
				for (Map.Entry<Integer,double[]> record : kados.getValue().entrySet()) 
				{
					System.out.println("userId = " + record.getKey() + " coef = " + Arrays.toString(record.getValue()));
				}
			}
			
		}
		
	}
	
	public void addCoef(double[] coef,Integer neighbourIndex)
	{
		double result = 0;
		
		for (Map.Entry<Double[],Hashtable<Double,Map<Integer,double[]>>> entry : coefs.entrySet()) 
		{
			
			result = getHashValue(coef, entry.getKey(),coef.length);
			
			if(entry.getValue().containsKey(result))
			{
				entry.getValue().get(result).put(neighbourIndex,coef);
			}
			else
			{
				Map<Integer,double[]> a  = new HashMap<Integer,double[]>();
				a.put(neighbourIndex,coef);
				entry.getValue().put(result, a);
			}
			
		}
		
	}
	public Map<Integer,int[]> find_neigh_overal(int users,int items, int[][][] dt, Double[] averageOverallArray)
	{
		Map<Integer,int[]> users_common_items = new HashMap<Integer, int[]>();
		
		for (int u = 0; u < users; u++) {
			
			initializeOveralls(4);

			Map<Integer,Double> commonItems = new HashMap<Integer,Double>();
			
			int common_items_counter = 0;
			for (int j = 0; j < items; j++)
			{
				for (int i = 0; i < users; i++ )
				{
					if(dt[u][j][0] != -1 && dt[i][j][0] != -1 && i != u)
					{
															//eyresh koinwn antikeimenwn metaksy users
						if(!commonItems.containsKey(i)){
							common_items_counter++;	
							commonItems.put(i,averageOverallArray[i]);
							addOverall(averageOverallArray[i],i);
							
						}
					}
				}
			}
			addOverall(averageOverallArray[u],u);
			int[] indexOfNeigboursMap = getNeighbourOverall(u);
//			System.out.println("counter" + common_items_counter);
//			System.out.println(Arrays.toString(indexOfNeigboursMap));
			users_common_items.put(u, indexOfNeigboursMap);
		}
		return users_common_items;
	}
	
	public int[] getNeighbourOverall(int userId)
	{
		
		int indexOfNeighbours[];
		Map<Integer,int[]> map = new HashMap<Integer,int[]>();
		
		for (Map.Entry<Double,Hashtable<Double,Map<Integer,Double>>> hashTable : overalls.entrySet()) 
		{
			for (Map.Entry<Double,Map<Integer,Double>> kados : hashTable.getValue().entrySet()) 
			{
				Map<Integer,Double> tempKados = kados.getValue();
				
				for (Integer indexOfNeighbour : tempKados.keySet()) 
				{
					int i = 0;
					
					indexOfNeighbours = new int[Math.max(1,tempKados.size()-1)];
					indexOfNeighbours[0] = -1;
					
					for (Integer indexOfNeighbour2 : tempKados.keySet()) 
					{
						if(indexOfNeighbour2 != indexOfNeighbour)
						{
							indexOfNeighbours[i] = indexOfNeighbour2;
							i++;
						}
					}
					
					map.put(indexOfNeighbour, indexOfNeighbours);
				}
					
			}
			
		}
		//System.out.println(Arrays.toString(map.get(userId)));
		return map.get(userId);
	}

	
	public void initializeCoef(int number_of_hash_tables)
	{
		coefs = new HashMap<Double[],Hashtable<Double,Map<Integer,double[]>>>();
		
		Double[] random_vector = new Double[0];
		
		for (int i = 0; i < number_of_hash_tables; i++) 
		{
			
			random_vector =	generate_random_unit_vector(8);
			
			Hashtable<Double, Map<Integer,double[]>> hash_table = new Hashtable<Double, Map<Integer,double[]>>();

			coefs.put(random_vector,hash_table);

		}
	}
	
	/*
	 * Arxikopoiei ta HashTables . Paragei ena tyxaio 7-diastato dianysma pou xrhsimopoieite
	 * gia na ypologistei h hashValue to ka8e stoixeiou mesa sto table, sto opoio to a8roisma twn
	 * syntetagmenwn tou einai 1.
	 * 
	 * @param number_of_hash_tables . To plh8os twn HashTables pou 8elw na paragw
	 * 
	 */
	public void initialize(int number_of_hash_tables){
		
		if( centroids == null)
		{
			Users users = new Users();
		}
			
		centroids = new HashMap<Double[],Integer>();
		
		map = new HashMap<Double[],Hashtable<Long,ArrayList<int[]>>>();
		
		d_map = new HashMap<Double[],Hashtable<Double,ArrayList<Double[]>>>();
		
		Double[] random_vector = new Double[7];
		
		random_vector =	generate_random_unit_vector();
		
		for (int i = 0; i < number_of_hash_tables; i++) 
		{
			
			random_vector =	generate_random_unit_vector();
			
			Hashtable<Long, ArrayList<int[]>> hash_table = new Hashtable<Long, ArrayList<int[]>>();
			
			Hashtable<Double, ArrayList<Double[]>> d_hash_table = new Hashtable<Double, ArrayList<Double[]>>();

			map.put(random_vector,hash_table);
			
			d_map.put(random_vector, d_hash_table);

		}
		
	}
	
	public void generateCentroids(int[][] newdataset,Integer userId, Double[] centroid)
	{
//		System.out.println("centroid = " + Arrays.toString(centroid));
		if( centroid != null )
		{
			add_point(centroid);
			return;
		}
		
		Integer itemsCounter=0;
		Integer overal;
		Integer krithria;
		Integer items = newdataset.length;
		Integer ratings = newdataset[0].length;
		Double[] c = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};

		for (int j = 0; j < items; j++) {
			//		for (Integer[] item_ratings : user.getValue()) 
			Double minDiafora = 5.0;
			Double maxDiafora = 0.0;

			overal = newdataset[j][0];
			if( overal == -1 )
				continue;
			for (int k = 1; k < ratings; k++) 
			{
				krithria = newdataset[j][k];
				
				c[k-1] += (double)overal/(double)krithria;
				
				if( c[k-1] >= maxDiafora )
					maxDiafora = c[k-1];

				if( c[k-1] <= minDiafora )
					minDiafora = c[k-1];

			}

			c[ratings-1] = maxDiafora - minDiafora;

			itemsCounter++;

		}

		for (int l = 0; l < c.length; l++) {
			c[l] = c[l]/itemsCounter;
		}

		centroids.put(c,userId);

		add_point(c);

	}

}
