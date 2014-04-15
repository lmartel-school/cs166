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

Wed 4/9 (Lecture 4)
===================

Order Statistics
----------------
kth order statistic: kth smallest element (0 indexed; 0th order statistic is min)

##### Question: how to keep track of order statistics while allowing insertions?

Answer: **Order Statistic Tree**, red/black tree with node annotation: Ax = # nodes in *left subtree*
- To find kth order statistic:
    - If k < Ax: go left
    - If k == Ax: found! a is kth order statistic
    - If k > Ax: k -= (Ax + 1), go right
- Insertions:
    - While finding insertion location, increment Ax every time you move to a left child
- Rotations:
    - To rotate A <-- B to A --> B (right rotation):
        - A's left subtree unchanged: Ax = Ax
        - B's left subtree: A's old right subtree
            - Bx = Bx - Ax - 1
    - Similarly, to rotate A --> B to A <-- B (left rotation):
        - A's left subtree unchanged: Ax = Ax
        - B's left subtree: A + A's old subtree + B's old subtree
            - Bx = 1 + Ax + Bx

### General pattern
- Order Statistic Tree works because the additional information changes **only in two cases**:
    - Along the root-leaf access path
    - During rotations

Augmented Red/Black Trees
-------------------------
- Let f(node) be a function such that:
    - f runs in O(1) time
    - f can be computed on node a using only:
        - a.value 
        - f(a.left)
        - f(a.right)
- Then, values of f can be cached in a red-black tree without changing the runtime of insertions and deletions.

Dynamic 1D Closest Points
-------------------------
- Augmented Red/Black Tree with:
    + Min
    + Max
    + Closest two points
- See slides for explanation

### Useful intuition
- Need a divide-and-conquer algorithm with O(1) time per "conquer" step

BSTs: Join and Split
--------------------
- join(T1, k, T2) takes two trees and a key:
    + Assumes (all T1 keys) < k < (all T2 keys)
    + Destructively modifies T1, T2 into one tree T containing:
        * All T1 keys
        * k
        * All T2 keys
    + Naive runtime (rebuild tree): O(n)
    + Clever runtime: O(1 + |h1 - h2|)
        * where h1, h2 heights of trees
    + 
- split(T, k) takes one tree and a key:
    + Destructively modifies T into two trees
        * T1 (all keys <= k)
        * T2 (all keys > k)

Mon 4/14 (Lecture 5)
====================

Dynamic Connectivity
--------------------
Tell if two nodes in graph are connected, maintain this knowledge through edge insertions and deletions

Dynamic Connectivity in Forests
-------------------------------
Special case: adding edges never creates a cycle, only connects trees. (And so deleting always breaks a tree into two trees)

Goal:
- link(u, v): adds edge {u, v}. The assumption is that u, v are in separate trees.
- cut(u, v): removes edge {u, v}. The assumption is that the edge exists in the tree.
- is-connected(u, v): return whether u, v are connected in the graph.
- RUNTIME: all these operations should run in O(log n) time.

Euler Tour
----------
Def: in a graph G, an Euler tour is a path through the graph that crosses every edge exactly once.

##### Representing a tree as an Euler tour
Trees don't have Euler tours.
    - Replace every edge {u, v} with two edges {u, v},{v, u}
    - Resulting graph has an Euler tour.
    - Sequence of nodes visited is Euler tour starting and ending at same node.
    - Interpret all edges as going AWAY from the root.

### Rerooting an Euler Tour
- Split the tour into three parts, S1 R S2 where R consists of the nodes between the first and last occurrences of the new root r
- Delete the first node in S1
- Concatenate: R S2 S1 {r}

Euler Tours and Dynamic Trees
-----------------------------
Given two trees T1, T2 with u in T1 and v in T2:
- link(u, v):
    + reroot(Euler(T1), u)
    + reroot(Euler(T2), v)
    + add directed edge {u, v}
    + concatenate: Euler(T1) Euler(T2) {u}
    + result is Euler(T) rooted at u, where T = T1 + T2 + Edge{u,v}
        * RUNTIME: O(1) splits/joins
Given a tree T containing edge {u, v}:
- cut(u, v):
    + reroot(Euler(T), u)
    + remove subtour from first(v) to last(v), keep as Euler(T2) rooted at v
    + Remainder of original tour is Euler(T1) rooted at u
        * RUNTIME: O(1) splits/joins

Implementation of Euler Tour
----------------------------
Use balanced tree (red/black tree)
- Represent each tree as Euler tour
- Store these sequences as balanced binary trees
- Each node in the original forest stores a pointer to its first and last occurrences in the red/black trees
- Each node in the red/black trees stores a pointer to its parent
    + is-connected(u, v): check if u, v contained in same red/black tree
        * O(log n)
    + link(u, v) = R/B tree join
        * O(log n)
    + cut(u, v) = R/B tree split
        * O(log n)

Maximum-Bottleneck Paths
========================
Problem: given a graph with weighted edges, find path from u to v with maximum bottleneck
- That is, maximize the minimum edge weight in the path
- Proved:
    + The maximum-bottleneck path between any two nodes in any graph is fully contained in the maximum spanning tree of that graph.
    + So if you can solve maximum-bottleneck for MST, you can solve for graph
Full algorithm:
- Preprocessing:
    + Compute maximum spanning tree T* in time O(m + n log n) 
        * [possible with fibonacci heaps]
    + Construct Cartesian tree of T* in time O(n log n)
    + Construct LCA data structure for that Cartesian tree in time O(n)
    + Total preprocessing O(m + n log n)
- Query for bottleneck edge between u and v:
    + Compute LCA of u, v
    + Time: O(1)

    