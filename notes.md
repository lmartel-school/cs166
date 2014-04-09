Mon 4/7 (Lecture 3)
===================

Binary Search Trees
-------------------
Deleting from a BST
0. No children - just delete
1. One child - delete node, replace with its child
2. Two children - find successor of the node, replace node with succ, delete succ (which must have 1 or 0 children)
    - Find succ: step right, step left-left-left until stuck

Time complexity of these operations (find, delete etc) is O(h) where h is height of the tree.
How to keep h small?

B-Tree
------
A B-Tree of order b is a multiway search tree with additional properties:
- All leaf nodes are stored at the same depth
- The root has between 1 and 2b-1 keys
- All non-root nodes have between b-1 and 2b-1 keys

Benefit: maximum height of B-tree of order b containing n nodes is log_b((n+1)/2) = O(log_b n)

Used in databases, filesystems (Note: people usually use B+ Trees instead; similar.)

Operations on a B-Tree
- Searching
    - Within a node, can find key or correct child in O(log b) time using binary search
    - We repeat this process at most O(h) = O(log_b n) times
    - Total runtime = O(log b * log_b n) = O(n)
    - But we only need to read O(log_b n) blocks from disk
- Insertion
    - Usually straightforward until a node gets too big
    - When node too big: split node in two, propagate upward
- Deletion
    - Usually straightforward until a node gets too small
    - When node too small: steal keys from adjacent nodes, or merge nodes if both are small

### 2-3-4 Tree: B-Tree with b = 2 (nodes have 2, 3, or 4 children)

Red/Black Tree
--------------
(Trivia: named because original authors had a printer with two colors, red and black)

Properties:
- Every node is red or black
- The root is black
- No red node has a red child
- **Every root-null path in the tree passes through the same number of black nodes.**
    - Root-null path: any path from the root to any null child

### THRM: any red/black tree with n nodes has height O(log n)
Proof intuition: A red/black tree is an isometry of a 2/3/4 tree (different representation of same structure)
