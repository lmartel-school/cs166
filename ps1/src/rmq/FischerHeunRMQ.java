package rmq;

import java.util.Arrays;
import java.util.Stack;

/**
 * An &lt;O(n), O(1)&gt; implementation of the Fischer-Heun RMQ data structure.
 *
 * You will implement this class for problem 3.iv of Problem Set One.
 */
public class FischerHeunRMQ implements RMQ {


    private final float[] elems;
    private int b;
    private final int[] blockMinIndices;
    private final float[] blockMinValues;
    private final SparseTableRMQ blocksTable;
    private final PrecomputedRMQ[] blockRMQs;

    /**
     * Creates a new FischerHeunRMQ structure to answer queries about the
     * array given by elems.
     *
     * @elems The array over which RMQ should be computed.
     */
    public FischerHeunRMQ(float[] elems) {
        this.elems = elems;
        b = (int) (log2(elems.length) / 4.0);

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
        for(int k = 0; k < nBlocks; k++){
            blockMinValues[k] = elems[blockMinIndices[k]];
        }
        blocksTable = new SparseTableRMQ(blockMinValues);

        // Now, precompute RMQ for each block
        PrecomputedRMQ[] precomputedRMQs = new PrecomputedRMQ[(int) Math.pow(4, b)];
        blockRMQs = new PrecomputedRMQ[nBlocks];

        for(int i = 0; i < nBlocks; i++){
            int start = b * i;
            int end = Math.min(b * (i + 1), elems.length);
            int t = cartesianTreeNumber(elems, start, end);
            PrecomputedRMQ thisBlockRMQ;
            if(precomputedRMQs[t] == null){
                thisBlockRMQ = new PrecomputedRMQ(Arrays.copyOfRange(elems, start, end));
                precomputedRMQs[t] = thisBlockRMQ;
            } else {
                thisBlockRMQ = precomputedRMQs[t];
            }
            blockRMQs[i] = thisBlockRMQ;
        }
    }

    /**
     * Evaluates RMQ(i, j) over the array stored by the constructor, returning
     * the index of the minimum value in that range.
     */
    @Override
    public int rmq(int i, int j) {
        int firstPartialBlock = blockNumberOfIndex(i);
        int frontBookendStop = Math.min(b * (firstPartialBlock + 1) - 1, j);
        int frontBookendMinIndex = i - (i % b) + blockRMQs[firstPartialBlock].rmq(i % b, frontBookendStop % b);

        int lastPartialBlock = blockNumberOfIndex(j);
        int backBookendStart = Math.max(b * lastPartialBlock, i);
        int backBookendMinIndex = backBookendStart - (backBookendStart % b) + blockRMQs[lastPartialBlock].rmq(backBookendStart % b, j % b);

        int minIndex = (elems[frontBookendMinIndex] < elems[backBookendMinIndex] ? frontBookendMinIndex : backBookendMinIndex);

        // If there are any blocks in the middle, ask the sparse table
        if(lastPartialBlock - firstPartialBlock > 1){
            int minIndexWithinBlocks = blocksTable.rmq(firstPartialBlock + 1, lastPartialBlock - 1);
            if(blockMinValues[minIndexWithinBlocks] < elems[minIndex]) minIndex = blockMinIndices[minIndexWithinBlocks];
        }

        return minIndex;
    }

    private int cartesianTreeNumber(float[] elems, int start, int end){
        Stack<Float> stack = new Stack<Float>();

        StringBuilder bits = new StringBuilder("");
        for(int i = start; i < end; i++){
            float f = elems[i];
            while(true){
                if(stack.empty() || stack.peek() < f) break;
                stack.pop();
                bits.append('0');
            }
            stack.push(f);
            bits.append('1');
        }
        for(int i = 0; i < stack.size(); i++) bits.append('0');

        return bits.length() > 0 ? Integer.parseInt(bits.toString(), 2) : 0;
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
