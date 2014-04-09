package rmq;
/**
 * An &lt;O(n<sup>2</sup>), O(1)&gt; implementation of RMQ that precomputes the
 * value of RMQ_A(i, j) for all possible i and j.
 *
 * You will implement this class for problem 3.i of Problem Set One.
 */
public class PrecomputedRMQ implements RMQ {

    private final int[][] indices;

    /**
     * Creates a new PrecomputedRMQ structure to answer queries about the
     * array given by elems.
     *
     * @elems The array over which RMQ should be computed.
     */
    public PrecomputedRMQ(float[] elems) {
        indices = new int[elems.length][elems.length];

        // Fill in diagonal
        for (int i = 0; i < elems.length; i++) {
            indices[i][i] = i;
        }

        // Fill in the rest with DP
        for (int gap = 1; gap < elems.length; gap++) {
            for (int i = 0; i < elems.length - gap; i++) {
                int j = i + gap;
                int leftI = indices[i][j-1];
                int downI = indices[i+1][j];
                indices[i][j] = elems[leftI] < elems[downI] ? leftI : downI;
            }
        }
    }

    /**
     * Evaluates RMQ(i, j) over the array stored by the constructor, returning
     * the index of the minimum value in that range.
     */
    @Override
    public int rmq(int i, int j) {
    	return indices[i][j];
    }
}
