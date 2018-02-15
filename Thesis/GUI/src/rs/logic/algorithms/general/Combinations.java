package rs.logic.algorithms.general;

import java.util.List;

/**
 * Calculates all combinations of k items from an array of n elements
 *
 * @author Simon Tzanakis
 */
public class Combinations
{
    /** Contains the counter of the combinations calculated */
    private static int count = 0;

    /**
     *
     * @param count Sets the static variable of the class count to parameter count
     */
    public void setCount(int count)
    {
        Combinations.count = count;
    }
    
    /**
     * Calculates combinations and adds them to a list
     *
     * @param n Length of the list to get the combinations from
     * @param loopno The same as k
     * @param ini Value that initializes where to start form, usually it's 0
     * @param a Empty array with size k
     * @param k Number of items that we will find the combinations for
     * @param list The list containing arrays of integers with the combinations found
     * @param max_combinations The maximum combinations to be counted
     * @return Returns the number of combinations found
     */
    public int findCombinations(int n, int loopno, int ini, int[] a, int k, List<int[]> list, int max_combinations)
    {
        int i;
        loopno--;
        if (loopno < 0)
        {
            a[k - 1] = ini;
            list.add(a.clone());
            count++;
            return 0;
        }
        for (i = ini; i < n - loopno - 1; i++)
        {
            a[k - 1 - loopno] = i;
            findCombinations(n, loopno, i + 1, a, k, list, max_combinations);
            if(count >= max_combinations)
            {
                return count;
            }
        }
        if (ini == 0)
        {
            return count;
        }
        else
        {
            return 0;
        }
    }
}
