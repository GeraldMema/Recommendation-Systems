package ann_decision_version;

/**
 * LICENCE AND COPYRIGHT : Walt Fair, Jr. http://www.codeproject.com/KB/recipes/LinReg.aspx
 *<p>
 * REFERENCES:
 * Draper, N. R. and H. Smith, Applied Regression Analysis, New York: Wiley (1966)
 *<p>
 * Embedded from C# to JAVA
 *<p>
 * @author Simon Tzanakis
 */
public class LinearRegression
{
    private double[][] V;           // Least squares and var/covar matrix
    private double[] C;             // Coefficients
    private double[] SEC;           // Std Error of coefficients
    private double RYSQ;            // Multiple correlation coefficient
    private double SDV;             // Standard deviation of errors
    private double FReg;            // Fisher F statistic for regression
    private double[] Ycalc;         // Calculated values of Y
    private double[] DY;            // Residual values of Y

    public double[][] getV()
    {
        return V;
    }

    public double[] getC()
    {
        return C;
    }

    public double[] getSEC()
    {
        return SEC;
    }

    public double getRYSQ()
    {
        return RYSQ;
    }

    public double getSDV()
    {
        return SDV;
    }

    public double getFReg()
    {
        return FReg;
    }

    public double[] getYcalc()
    {
        return Ycalc;
    }

    public double[] getDY()
    {
        return DY;
    }

    /**
     *
     * @param Y Matrix with the data points
     * @param X Matrix with the values of independents variables of the datapoints
     * @param W Matrix containing weightes
     * @return Returns true on success, false on failure
     */
    public boolean regress(double[] Y, double[][] X, double[] W)
    {
        // Y[j]   = j-th observed data point
        // X[i,j] = j-th value of the i-th independent varialble
        // W[j]   = j-th weight value

        int M = Y.length;                   // M = Number of data points
        int N = X.length * X[0].length / M; // N = Number of linear terms
        int NDF = M - N;                    // Degrees of freedom
        Ycalc = new double[M];
        DY = new double[M];
        // If not enough data, don't attempt regression
        if (NDF < 1)
        {
            return false;
        }
        V = new double[N][N]; // Least squares and var/covar matrix
        C = new double[N];
        SEC = new double[N]; // sec = std error for coefficients
        double[] B = new double[N];   // Vector for LSQ

        // Clear the matrices to start out
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                V[i][j] = 0;
            }
        }

        // Form Least Squares Matrix
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                V[i][j] = 0;
                for (int k = 0; k < M; k++)
                {
                    V[i][j] = V[i][j] + W[k] * X[i][k] * X[j][k];
                }
            }

            B[i] = 0;
            for (int k = 0; k < M; k++)
            {
                B[i] = B[i] + W[k] * X[i][k] * Y[k];
            }
        }

        // V now contains the raw least squares matrix
        if (!SymmetricMatrixInvert(V))
        {
            return false;
        }

        // V now contains the inverted least square matrix
        // Matrix multpily to get coefficients C = VB
        for (int i = 0; i < N; i++)
        {
            C[i] = 0;
            for (int j = 0; j < N; j++)
            {
                C[i] = C[i] + V[i][j] * B[j];
            }
        }

        // Calculate statistics
        double TSS = 0;
        double RSS = 0;
        double YBAR = 0;
        double WSUM = 0;
        for (int k = 0; k < M; k++)
        {
            YBAR = YBAR + W[k] * Y[k];
            WSUM = WSUM + W[k];
        }
        YBAR = YBAR / WSUM;
        for (int k = 0; k < M; k++)
        {
            Ycalc[k] = 0;
            for (int i = 0; i < N; i++)
            {
                Ycalc[k] = Ycalc[k] + C[i] * X[i][k];
            }
            DY[k] = Ycalc[k] - Y[k];
            TSS = TSS + W[k] * (Y[k] - YBAR) * (Y[k] - YBAR);
            RSS = RSS + W[k] * DY[k] * DY[k];
        }

        double SSQ = RSS / NDF;
        RYSQ = 1 - RSS / TSS;
        FReg = 9999999;
        if (RYSQ < 0.9999999)
        {
            FReg = RYSQ / (1 - RYSQ) * NDF / (N - 1);
        }
        SDV = Math.sqrt(SSQ);

        // Calculate var-covar matrix and std error of coefficients
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                V[i][j] = V[i][j] * SSQ;
            }
            SEC[i] = Math.sqrt(V[i][i]);
        }
        return true;
    }

    /**
     * Inverts a symmetric matrix
     *
     * @param V The 2D matrix to be inverted
     * @return Returns true on success or false on failure
     */
    public boolean SymmetricMatrixInvert(double[][] V)
    {
        int N = (int) Math.sqrt(V.length * V[0].length);
        double[] t = new double[N];
        double[] Q = new double[N];
        double[] R = new double[N];
        double AB = 0;
        int K, L, M;

        // Invert a symetric matrix in V
        for (M = 0; M < N; M++)
        {
            R[M] = 1;
        }
        K = 0;
        for (M = 0; M < N; M++)
        {
            double Big = 0;
            for (L = 0; L < N; L++)
            {
                AB = Math.abs(V[L][L]);
                if ((AB > Big) && (R[L] != 0))
                {
                    Big = AB;
                    K = L;
                }
            }
            if (Big == 0)
            {
                return false;
            }

            R[K] = 0;
            Q[K] = 1 / V[K][K];
            t[K] = 1;
            V[K][K] = 0;
            if (K != 0)
            {
                for (L = 0; L < K; L++)
                {
                    t[L] = V[L][K];
                    if (R[L] == 0)
                    {
                        Q[L] = V[L][K] * Q[K];
                    }
                    else
                    {
                        Q[L] = -V[L][K] * Q[K];
                    }
                    V[L][K] = 0;
                }
            }

            if ((K + 1) < N)
            {
                for (L = K + 1; L < N; L++)
                {
                    if (R[L] != 0)
                    {
                        t[L] = V[K][L];
                    }
                    else
                    {
                        t[L] = -V[K][L];
                    }
                    Q[L] = -V[K][L] * Q[K];
                    V[K][L] = 0;
                }
            }

            for (L = 0; L < N; L++)
            {
                for (K = L; K < N; K++)
                {
                    V[L][K] = V[L][K] + t[L] * Q[K];
                }
            }
        }
        M = N;
        L = N - 1;
        for (K = 1; K < N; K++)
        {
            M = M - 1;
            L = L - 1;
            for (int J = 0; J <= L; J++)
            {
                V[M][J] = V[J][M];
            }
        }
        return true;
    }
}