/**
 * An interface representing a static binary search tree over the keys 0, ..., n - 1.
 * <p/>
 * Any class that implements this interface should have a constructor of the form
 * <p/>
 * public ClassName(double[] weights);
 * <p/>
 * This method should contain the weights that will be assigned to the values 0, ..., n - 1.
 */
public interface BST {
    /**
     * Returns whether the specified value is contained within the BST.
     *
     * @param value The value to look up.
     * @return Whether that value is present.
     */
    public boolean contains(int value);
}
