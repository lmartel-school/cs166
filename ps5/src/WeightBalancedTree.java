/**
 * An implementation of a static BST backed by a weight-balanced tree.
 */
public class WeightBalancedTree implements BST {
    TreeNode root;

    public WeightBalancedTree(double[] elements) {
        if (elements.length == 0) return;
        double sum = 0;

        /* Compute initial sum. */
        for (int i = 0; i < elements.length; i++) {
            sum += elements[i];
        }

        root = new TreeNode(elements, 0, elements.length, sum);
        assert(root.isValidBinaryTree());
    }

    /**
     * Returns whether the specified key is in the BST.
     *
     * @param key The key to test.
     * @return Whether it's in the BST.
     */
    public boolean contains(int key) {
        TreeNode node;

        for (node = root; node != null && node.getKey() != key; ) {
            if (key < node.getKey()) {
                node = node.getLeftTree();
            } else {
                node = node.getRightTree();
            }
        }

        if (node == null) {
            return false;
        }

        return true;
    }

    private class TreeNode {
        private int key;
        private TreeNode leftTree;
        private TreeNode rightTree;

        public int computeDepth() {
            int depth = 1;

            if (leftTree != null) {
                depth += leftTree.computeDepth();
            }

            if (rightTree != null) {
                int rightTreeDepth = rightTree.computeDepth();
                depth = Math.max(depth, 1 + rightTreeDepth);
            }

            return depth;
        }

        /**
         * Constructs a new tree from the specified array of weights. The array entry
         * at position 0 specifies the weight of 0, the entry at position 1 specifies
         * the weight of 1, etc.
         *
         * @param elements The weights on the elements.
         * @param start    inclusive start index
         * @param end      exclusive ending index
         */
        public TreeNode(double[] elements, int start, int end, double rightSum) {
            int left;
            double leftSum = 0;
            double difference = Math.abs(rightSum - leftSum);

            for (left = start; left < end && newDifference(elements, leftSum, rightSum, left) < difference; left++) {
                leftSum += elements[left];
                rightSum -= elements[left];

                difference = Math.abs(rightSum - leftSum);
            }

            /* Clamp the split pointer to the bounds of the array -- in some cases it may wander too far right. */
            left = Math.min(Math.max(start, left), end - 1);

            System.out.println("Splitting at: (" + left + "/" + elements.length + ")");

            this.key = left;

        /* Doin' some arm's length recursion here. */
            if (start < left) {
                leftTree = new TreeNode(elements, start, left, leftSum);
            }
            if (left + 1 < end) {
                rightTree = new TreeNode(elements, left + 1, end, rightSum);
            }
        }

        private double newDifference(double[] elements, double leftSum, double rightSum, int left) {
            return Math.abs((leftSum + elements[left]) - (rightSum - elements[left]));
        }

        public boolean isValidBinaryTree() {
            boolean valid = true;

            if (leftTree != null) {
                if (getKey() < leftTree.getKey()) valid = false;

                valid = valid && leftTree.isValidBinaryTree();
            }

            if (rightTree != null) {
                if (getKey() > rightTree.getKey()) valid = false;
                valid = valid && rightTree.isValidBinaryTree();
            }

            return valid;
        }



        public int getKey() {
            return key;
        }

        public TreeNode getLeftTree() {
            return leftTree;
        }

        public TreeNode getRightTree() {
            return rightTree;
        }
    }
}
