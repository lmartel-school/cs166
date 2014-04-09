package rmq;
/**
 * An &lt;O(n), O(log n)&gt; implementation of the RMQ as a hybrid between
 * the sparse table (on top) and no-precomputation structure (on bottom)
 *
 * You will implement this class for problem 3.iii of Problem Set One.
 */
public class HybridRMQ implements RMQ {

    private final float[] elems;
    private int b;
    private final int[] blockMinIndices;
    private final float[] blockMinValues;
    private final SparseTableRMQ blocksTable;

    /**
     * Creates a new HybridRMQ structure to answer queries about the
     * array given by elems.
     *
     * @elems The array over which RMQ should be computed.
     */
    public HybridRMQ(float[] elems) {
        this.elems = elems;

        b = (int) log2(elems.length); // (int) cast truncates

        // blocks should exist
        b = Math.max(b, 1);
        int nBlocks = (int) Math.ceil(elems.length / (double) b);
        blockMinIndices = new int[nBlocks];

        // Build blocks: O(n) work
        for(int i = 0, blockNum = 0; blockNum < nBlocks; blockNum++){
            int indexOfBlockMin = i;
            for(int blockProgress = 0; blockProgress < b && i < elems.length; blockProgress++, i++){
                if(elems[i] < elems[indexOfBlockMin]) indexOfBlockMin = i;
            }
            blockMinIndices[blockNum] = indexOfBlockMin;
        }

        blockMinValues = new float[nBlocks];
        for(int i = 0; i < nBlocks; i++){
            blockMinValues[i] = elems[blockMinIndices[i]];
        }
        blocksTable = new SparseTableRMQ(blockMinValues);
    }

    /**
     * Evaluates RMQ(i, j) over the array stored by the constructor, returning
     * the index of the minimum value in that range.
     */
    @Override
    public int rmq(int i, int j) {
        int firstPartialBlock = blockNumberOfIndex(i);
        int frontBookendMinIndex = i;

        // Go until the end of the first partial block (but not past j)
        for(int cursor = i; blockNumberOfIndex(cursor) == firstPartialBlock && cursor <= j; cursor++){
            if(elems[cursor] < elems[frontBookendMinIndex]) frontBookendMinIndex = cursor;
        }

        int lastPartialBlock = blockNumberOfIndex(j);
        int backBookendMinIndex = j;

        // Start at the beginning of the last partial block (but not before i)
        for(int cursor = Math.max(lastPartialBlock * b, i); cursor <= j; cursor++){
            if(elems[cursor] < elems[backBookendMinIndex]) backBookendMinIndex = cursor;
        }

        int minIndex = (elems[frontBookendMinIndex] < elems[backBookendMinIndex] ? frontBookendMinIndex : backBookendMinIndex);

        // If there are any blocks in the middle, ask the sparse table
        if(lastPartialBlock - firstPartialBlock > 1){
            int minIndexWithinBlocks = blocksTable.rmq(firstPartialBlock + 1, lastPartialBlock - 1);
            if(blockMinValues[minIndexWithinBlocks] < elems[minIndex]) minIndex = blockMinIndices[minIndexWithinBlocks];
        }

        return minIndex;
    }

    // Just making sure
    private int blockNumberOfIndex(int i){
        return i / b;
    }

    // How is this not a library function
    private double log2(double n){
        return Math.log(n) / Math.log(2);
    }
}
