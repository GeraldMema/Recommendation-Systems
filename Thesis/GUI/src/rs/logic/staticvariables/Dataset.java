package rs.logic.staticvariables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import rs.logic.algorithms.steps.LinearRegression;

/**
 * Class containing the dataset values
 *
 * @author Simon Tzanakis
 */
public class Dataset
{
    private static int[][][] dataset;
    private static int[][][] newdataset;
    private static int X;
    private static int Y;
    private static int Z;
    private static int MAX_R;
    
    static Map<Integer, Integer> mpUsers;
    
    public static Integer getUserId(Integer i)
    {
    	for (Map.Entry<Integer, Integer> userId : mpUsers.entrySet()) {
			if( userId.getValue().equals(i) )
				return userId.getKey();
		}
    	
    	System.err.println("no userId found dataset.getUserID");
    	return null;
    }

    /**
     * Checks if dataset exists(is null or not)
     *
     * @return Returns true if dataset exists, false if dataset doesn't exist
     */
    public static boolean datasetExists()
    {
        if (dataset != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static void setDataset(int[][][] dataset)
    {
        Dataset.dataset = dataset;
    }

    public static int[][][] getDataset()
    {
        return Dataset.dataset;
    }

    public static int[][][] getNewdataset()
    {
        return newdataset;
    }

    public static void setNewdataset(int[][][] newdataset)
    {
        Dataset.newdataset = newdataset;
    }

    /**
     * Copies the values of the dataset to the newdataset matrix
     */
    public static void setNewdatasetWithDataset()
    {
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                for (int k = 0; k < Z; k++)
                {
                    Dataset.newdataset[i][j][k] = Dataset.dataset[i][j][k];
                }
            }
        }
    }

    /**
     * Create the dataset matrices out of an SQL file
     *
     * @param url The url location of the SQL database
     * @param username The username used to login to the SQL datbase
     * @param password The password used to login to the SQL database
     * @throws SQLException
     * @throws InstantiationException Exception thrown from the jdbc driver
     * @throws IllegalAccessException Exception thrown from the jdbc driver
     * @throws ClassNotFoundException Exception thrown from the jdbc driver
     */
    public static void CreateDataset(String url, String username, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        //Connect to database
        String connectionURL = "jdbc:" + url;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, username, password);
        statement = connection.createStatement();

        rs = statement.executeQuery("SELECT * FROM meta_data");
        rs.next();
        X = rs.getInt("number_Users");
        Y = rs.getInt("number_Items");
        Z = rs.getInt("number_Ratings");
        MAX_R = rs.getInt("Max_Rating");

        int[][][] dt = new int[X][Y][Z];
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                for (int k = 0; k < Z; k++)
                {
                    dt[i][j][k] = -1;
                }
            }
        }
        mpUsers = new HashMap<Integer, Integer>();
        Map<Object, Object> mpHotels = new HashMap<Object, Object>();

//        rs = statement.executeQuery("SELECT * FROM users");
        rs = statement.executeQuery("SELECT * FROM users_items_ratings");
        int i = 0;
        while (rs.next())
        {
            if (mpUsers.containsKey(rs.getInt("UserID")) == false)
            {
                mpUsers.put(new Integer(rs.getInt("UserID")), new Integer(i));
                i++;
            }
        }

        rs = statement.executeQuery("SELECT * FROM users_items_ratings");
        int j = 0;
        int m = 0;
        while (rs.next())
        {
            i = (Integer) mpUsers.get(new Integer(rs.getInt("UserID"))); // τα i αντιπροσωπεύουν τους χρήστες
            if (mpHotels.containsKey(rs.getInt("ItemID")) == false)
            {
                mpHotels.put(new Integer(rs.getInt("ItemID")), new Integer(m));
                m++;
            }

            if (mpHotels.containsKey(rs.getInt("ItemID")) != false)
            {
                j = (Integer) mpHotels.get(new Integer(rs.getInt("ItemID")));
            }

            for (int k = 0; k < Z; k++)
            {
                if (k != 0 && rs.getInt("Rating" + k) == -1)
                {
                    dt[i][j][k] = dt[i][j][0];    //Set the -1 values to the overall rating
                }
                else
                {
                    dt[i][j][k] = rs.getInt("Rating" + k);
                }
            }
        }

        //Get the actuall number of users(Check for linear regression)
        int user_counter = 0;
        for (i = 0; i < X; i++)     //We pass the matrices for each user every time
        {
            int counter = 0; // counter κρατάει το πλήθος των αντικειμένων που έχει δώσει ολική βαθμολογία ο χρήστης
            for (j = 0; j < Y; j++) // για καθε αντικείμενο
            {
                if (dt[i][j][0] != -1) // εάν έχει ολική βαθμολογία ο χρήστης i για το αντικείμενο j
                {
                    counter++;
                }
            }
            if (counter != 0) // εαν ο χρήστης έχει βαθμολογήσει τουλάχιστον ένα αντικείμενο
            {
                int N = counter;  // N = πλήθος αντικειμένων που έχει δώσει ολική βαθμολογία ο χρήστης
                double[] y = new double[N];             //Are the overall ratings
                double[][] x = new double[Z][N];        //x[>0][i] are the multi-ratings except the overall
                double[] weight = new double[N];
                int u = 0;
                for (j = 0; j < Y; j++)  // για κάθε αντικείμενο
                {
                    if (dt[i][j][0] != -1) // εαν υπάρχει βασική βαθμολογία του χρήστη i για το αντικείμενο j
                    {
                        y[u] = dt[i][j][0];  // το y[u] κρατάει τις ολικές βαθμολογίες οι οποίες έχουν δωθεί
                        x[0][u] = 1;  // ο πίνακας x έχει 1 στη πρώτη θέση και μετά για το αντικείμενο που έχει ολική βαθμολογία αποθηκεύει τις επιμέρους βαθμολογίες
                        for (int k = 1; k < Z; k++) 
                        {
                            x[k][u] = dt[i][j][k]; // κράτα τις βαθμολογίες απο τα επιμέρους κριτήρια
                        }
                        weight[u] = 1; // το βάρος του αντικειμένου αρχικοποιείται σε 1
                        u++;
                    }
                }

                user_counter++;
                
            }
        }

        //Create the actuall dataset
        Dataset.dataset = new int[user_counter][Y][Z];
        Dataset.newdataset = new int[user_counter][Y][Z];
        for (i = 0; i < user_counter; i++)
        {
            for (j = 0; j < Y; j++)
            {
                for (int k = 0; k < Z; k++)
                {
                    Dataset.dataset[i][j][k] = -1;
                    Dataset.newdataset[i][j][k] = -1;
                }
            }
        }

        int s = 0;
        for (i = 0; i < X; i++)     //We pass the matrices for each user every time
        {
            int counter = 0;
            for (j = 0; j < Y; j++)
            {
                if (dt[i][j][0] != -1)
                {
                    counter++;
                }
            }
            if (counter != 0)
            {
                int N = counter;
                double[] y = new double[N];             //Are the overall ratings
                double[][] x = new double[Z][N];        //x[>0][i] are the multi-ratings except the overall
                double[] weight = new double[N];
                int u = 0;
                for (j = 0; j < Y; j++)
                {
                    if (dt[i][j][0] != -1)
                    {
                        y[u] = dt[i][j][0];
                        x[0][u] = 1;
                        for (int k = 1; k < Z; k++)
                        {
                            x[k][u] = dt[i][j][k];
                        }
                        weight[u] = 1;
                        u++;
                    }
                }
                for (int l = 0; l < Y; l++)
                {
                	for (int k = 0; k < Z; k++)
                	{
                		Dataset.dataset[s][l][k] = dt[i][l][k];
                	}
                }
                s++;
            }
        }
        X = user_counter;

    }

    public static int getXSize()
    {
        return X;
    }

    public static int getYSize()
    {
        return Y;
    }

    public static int getZSize()
    {
        return Z;
    }

    public static int getMAX_R()
    {
        return MAX_R;
    }
}
