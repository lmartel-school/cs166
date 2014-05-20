/**
 * An implementation of a BST backed by a splay tree.
 */
public class SplayTree implements BST {
    private TreeNode root;

    /**
     * Constructs a new tree from the specified array of weights. Since splay
     * trees don't care about access probabilities, you should only need
     * to read the length of the weights array and not the weights themselves.
     * This tree should store the values 0, 1, 2, ..., n - 1, where n is the length
     * of the input array.
     *
     * @param keys weights on the elements.
     */
    public SplayTree(double[] keys) {
        if (keys.length == 0) { return; }

        root = new TreeNode(null, 0, keys[0]);
        TreeNode current = root;
        for (int i = 1; i < keys.length; i++) {
            TreeNode newNode = new TreeNode(current, i, keys[i]);
            current.setRight(newNode);
            current = newNode;
        }
    }

    public String toString() {
        return root.toString();
    }


    /**
     * Returns whether the specified key is in the BST.
     *
     * @param key The key to test.
     * @return Whether it's in the BST.
     */
    public boolean contains(int key) {
        TreeNode node;
        TreeNode lastNode = null;

        for (node = root; node != null && node.getKey() != key; ) {
            if (key < node.getKey()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
            lastNode = node;
        }

        if (node == null) {
            if (lastNode != null) {
                splayToRoot(lastNode);
            }
            return false;
        }

        splayToRoot(node);
        return true;
    }

    /**
     * At the termination of this method, node should be the root of the tree.
     * @param node
     */
    private void splayToRoot(TreeNode node) {
        while (root != node) {
            node.splay();
        }

        assert isValidBinaryTree() : "Should be valid binary tree, but got: \n ";
    }

    public boolean isValidBinaryTree() {
        assert root.getParent() == null : "Root should have no parent.";
        return root.isValidBinaryTree();
    }

    private class TreeNode {
        private int key;
        private double weight;

        private TreeNode left;
        private TreeNode right;
        private TreeNode parent;

        public TreeNode(TreeNode parent, int key, double weight) {
            this.parent = parent;
            this.key = key;
            this.weight = weight;
        }

        public void splay() {
            /* A root-node should not be splayed. */
            assert(parent != null);

            TreeNode grandParent = getGrandParent();

            if (grandParent == null) {
                // zig
                this.rotate(parent);
            } else if ((grandParent.getRight() == parent && parent.getLeft() == this)
                    || (grandParent.getLeft() == parent && parent.getRight() == this)) {
                // zig-zag
                this.rotate(parent);
                this.rotate(grandParent);
            } else {
                // zig-zig
                parent.rotate(grandParent);
                this.rotate(parent);
            }
        }

        /* Properly detects whether or not we need left/right rotation */
        private void rotate(TreeNode parentNode) {
            assert (parentNode != null);
            assert this.getParent() == parentNode : "Can only rotate with parent.";
            assert parentNode.getLeft() == this || parentNode.getRight() == this : "Should be a child of the node to rotate with.";
            TreeNode newParent = parentNode.getParent();

            if (parentNode == root) {
                root = this;
            }

            if (parentNode.getLeft() == this) {
                /* We're doing a right-rotation */
                if (newParent != null) {
                    newParent.replaceChild(parentNode, this);
                } else {
                    setParent(null);
                }
                parentNode.setLeft(this.getRight());
                this.setRight(parentNode);
            } else {
                /* We're doing a left-rotation */
                if (newParent != null) {
                    newParent.replaceChild(parentNode, this);
                } else{
                    setParent(null);
                }
                parentNode.setRight(this.getLeft());
                this.setLeft(parentNode);
            }
        }

        private void replaceChild(TreeNode child, TreeNode newChild) {
            if (child == left) {
                setLeft(newChild);
            } else {
                setRight(newChild);
            }
        }

        public TreeNode getGrandParent() {
            if (parent == null) return null;

            return parent.getParent();
        }

        public int getKey() {
            return key;
        }

        public TreeNode getLeft() {
            return left;
        }

        public void setLeft(TreeNode left) {
            this.left = left;
            if (left != null) {
                left.setParent(this);
            }
        }

        public TreeNode getRight() {
            return right;
        }

        public void setRight(TreeNode right) {
            if (right != null) {
                right.setParent(this);
            }
            this.right = right;
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public boolean isValidBinaryTree() {
            boolean valid = true;

            if (left != null) {
                if (getKey() < left.getKey()) valid = false;
                if (this != left.getParent()) valid = false;

                valid = valid && left.isValidBinaryTree();
            }

            if (right != null) {
                if (getKey() > right.getKey()) valid = false;
                if (this != right.getParent()) valid = false;
                valid = valid && right.isValidBinaryTree();
            }

            return valid;
        }

        private void indent(int indentationLevel, StringBuilder builder) {
            for (int i = 0; i < indentationLevel; i++) {
                builder.append("\t");
            }
        }

        public void toString(int indentationLevel, StringBuilder builder) {
            builder.append(key + " --\n");
            indentationLevel++;
            if (left != null) {
                indent(indentationLevel, builder);
                builder.append("left: ");
                left.toString(indentationLevel, builder);
            }

            if (right != null) {
                indent(indentationLevel, builder);
                builder.append("right: ");
                right.toString(indentationLevel, builder);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("<Splay Tree>:\n");
            builder.append("root: " );
            toString(0, builder);

            return builder.toString();
        }

    }
}
