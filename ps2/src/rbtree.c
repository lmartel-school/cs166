#include "rbtree.h"
#include <stddef.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>

typedef struct node node;
const char RED = 1;
const char BLACK = 0;

char color(node *n){
    return ((uintptr_t)n->left) & 1;
}

char safe_color(node *n){
    if(n == NULL) return -1;
    return color(n);
}

node *left(node *n){
    return (node *) ((uintptr_t)n->left >> 1);
}

// for symmetry
node *right(node *n){
    return n->right;
}

// Return # blacks between us and NULL
// Return -1 for INVALID TREE
int check_red_black_tree(node* n){
    if(n == NULL) return 0;
    
    // Red node cannot have red children
    char myColor = color(n);
    if(myColor == RED){
        if(safe_color(left(n)) == RED ||
           safe_color(right(n)) == RED) return -1;
    }
    int leftBlacks = check_red_black_tree(left(n));
    int rightBlacks = check_red_black_tree(right(n));

    // Bubble failure up
    if(leftBlacks == -1 || rightBlacks == -1) return -1;

    // Equal # black nodes on every here-NULL path
    if(leftBlacks != rightBlacks) return -1;

    return rightBlacks + (myColor == BLACK ? 1 : 0);
}

/**
 * Function: is_red_black_tree(struct node* root);
 * --------------------------------------------------------------------------
 * Given a pointer to the root of a tree, returns whether that tree is a 
 * red/black tree. This function can assume the following:
 *
 * 1. The pointer provided either points to a valid address or to NULL.
 * 2. The tree structure pointed at is indeed a tree; there aren't any
 *    directed or undirected cycles and all internal pointers are valid
 *    (though the left pointers may need to be tweaked a bit to get to
 *    valid data).
 *
 * Why this function runs in time O(n):
 *      This is a recursive function that follows every parent->child pointer once.
 *      It therefore visits every node twice, once on the way down and once on the way up.
 *      It does constant work at each node (just a few math ops), 
 *      so the total runtime is 2n * O(1) = O(n).
 */
bool is_red_black_tree(struct node* root) {
	if(root == NULL) return false;
    if(color(root) == RED) return false;
    if(check_red_black_tree(root) == -1) return false;
    return true;
}

/**
 * Function: to_red_black_tree(int elems[], unsigned length);
 * -------------------------------------------------------------------------
 * Given as input a sorted array of elements, returns a new red/black tree
 * containing the elements of that array.
 *
 * TODO: Edit this comment to describe why this function runs in time O(n).
 */
struct node* to_red_black_tree(int elems[], unsigned length) {
	/* TODO: Implement this! */
	return NULL;
}
















