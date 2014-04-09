package rmq;

import java.util.Arrays;

/**
 * An &lt;O(n log n), O(1)&gt; implementation of RMQ that uses a sparse table
 * to do lookups efficiently.
 *
 * You will implement this class for problem 3.ii of Problem Set One.
 */
public class SparseTableRMQ implements RMQ {

    private final float[] elems;
    private final int[][] indices;
    private final int[] kLookup;

    /**
     * Creates a new SparseTableRMQ structure to answer queries about the
     * array given by elems.
     *
     * @elems The array over which RMQ should be computed.
     */
    public SparseTableRMQ(float[] elems) {
        this.elems = elems;

        // Set up O(1) log lookup table, O(n) work
        kLookup = new int[elems.length + 1];
        kLookup[0] = -1; // shouldn't ask for range of size 0
        for(int k = 0, p = 1, x = 1; x <= elems.length; x++){
            if(2*p <= x){
                k++;
                p *= 2;
            }
            kLookup[x] = k;
        }

        // Log base 2 of n, rounded up
        int maxPower = (int) Math.ceil(log2(elems.length));

        // Handle edge case of empty array
        maxPower = Math.max(maxPower, 0);
        indices = new int[elems.length][maxPower + 1];

        // -1 means out-of-bounds
        for(int[] a : indices){
            Arrays.fill(a, -1);
        }

        // Fill first row (2^0)
        for(int index = 0; index < elems.length; index++){
            indices[index][0] = index;
        }

        // Fill the rest with DP
        for(int power = 1, segLen = 2; power <= maxPower; power++, segLen *= 2){
            for(int index = 0; index <= elems.length - segLen; index++){

                // integer division; round down
                int secondI = index + (segLen / 2);
                int topElemIndex = indices[index][power - 1];
                int bottomElemIndex = indices[secondI][power - 1];
                indices[index][power] = elems[topElemIndex] < elems[bottomElemIndex] ? topElemIndex : bottomElemIndex;
            }
        }
    }

    /**
     * Evaluates RMQ(i, j) over the array stored by the constructor, returning
     * the index of the minimum value in that range.
     */
    @Override
    public int rmq(int i, int j) {
        int k = findK(i, j);

        // Simple case: one interval
        int segLen = (int) Math.pow(2, k);
        if(segLen == j - i + 1){
            return indices[i][k];
        }

        // Otherwise: two intervals
        int secondI = j + 1 - segLen;
        int topElemIndex = indices[i][k];
        int bottomElemIndex = indices[secondI][k];
        return elems[topElemIndex] < elems[bottomElemIndex] ? topElemIndex : bottomElemIndex;
    }

    private int findK(int i, int j){
        return kLookup[j - i + 1];
    }

    // How is this not a library function
    private double log2(double n){
        return Math.log(n) / Math.log(2);
    }
}
