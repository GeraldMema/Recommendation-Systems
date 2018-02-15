package rs.logic.staticvariables;

import java.util.Arrays;

/**
 * Class containing the coefficients values
 *
 * @author Simon Tzanakis
 */
public class Coefficients
{
	private static double[][] coefficients = null;
	private static int X;
	private static int Y;

	/**
	 *
	 * @param n Need to pass the n = #of users, items of rows
	 * @param m Need to pass m = #of ratings = OVERALL + MULTI-RATINGS, items on columns
	 */
	public static void Create_Coefficients_Matrix(int n, int m)
	{
		X = n;
		Y = m;
		Coefficients.coefficients = new double[n][m];
		for(int i = 0; i < n; i++)
		{
			for( int j = 0; j < m; j++)
			{
				Coefficients.coefficients[i][j] = -1;
			}
		}
	}

	public static double[][] getCoefficients()
	{
		return coefficients;
	}

	private static double[] normalize_Coef( double c[])
	{
		double negC = 0.0;
		double posC = 0.0;
		for (int i = 0; i < c.length; i++) {
			if(c[i] < 0)
				negC += c[i];
		}

		if( negC < 0){
			for (int i = 0; i < c.length; i++)			
				c[i] -= negC*2;						
		}

		//		System.out.println("C  " + Arrays.toString(c) );//+ " sunolo = " + sunolo);

		posC = 0.0;
		for (int i = 0; i < c.length; i++) {
			posC += c[i];
		}
		double sunolo = posC;

		if( sunolo != 0){
			for (int i = 0; i < c.length; i++)			
				c[i] /= sunolo;						
		}
		sunolo = 0.0;
		for (int i = 0; i < c.length; i++)
			sunolo += c[i];

//		if( sunolo == 0 )
//			return null;
		
		return c;

	}

	/**
	 *
	 * @param index Index to set the 1D coefficients matrix passed as a parameter on the 2D class coefficients matrix variable
	 * @param coef The 1D coeeficients matrix to be passed
	 */
	public static void setCoefficientsAtIndex(int index, double[] coef)
	{
		coef = Coefficients.normalize_Coef(coef);
		Coefficients.coefficients[index] = coef;
	}

	public static int getXSize()
	{
		return X;
	}

	public static int getYSize()
	{
		return Y;
	} 
}
