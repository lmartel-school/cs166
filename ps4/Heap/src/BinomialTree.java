public class BinomialTree {
    private int numNodes;
    private int key;
    private BinomialTree left;
    private BinomialTree right;

    /* Build a singleton tree */
    public BinomialTree(int key) {
        numNodes = 1;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public BinomialTree getLeft() {
        return left;
    }

    public BinomialTree getRight() {
        return right;
    }

    public void setLeft(BinomialTree left) {
        this.left = left;
    }

    public void setRight(BinomialTree right) {
        this.right = right;
    }

    /**
     * Assumed that child has no siblings.
     * @param child
     */
    public void addChild(BinomialTree child) {
        assert (child.getRight() == null);
        child.setRight(getLeft());
        this.left = child;
        this.numNodes += child.getSize();
    }

    /**
     * Returns the right-spine of the tree after removing the root. These are the root's child trees.
     * @return The root's child trees.
     */
    public MeldableLinkedList<BinomialTree> extractRoot() {
        MeldableLinkedList<BinomialTree> trees = new MeldableLinkedList<BinomialTree>();
        BinomialTree cursor = getLeft();
        while (cursor != null) {
            BinomialTree current = cursor;
            trees.append(cursor);

            /* Get the next tree in the list and disconnect the current tree from it. */
            cursor = cursor.getRight();
            current.setRight(null);
        }

        return trees;
    }

    public int getSize() {
        return numNodes;
    }

    public static BinomialTree coalesce(BinomialTree left, BinomialTree right) {
        if (left.getKey() > right.getKey()) {
            right.addChild(left);
            return right;
        } else {
            left.addChild(right);
            return left;
        }
    }
}
