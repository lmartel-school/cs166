#include "rbtree.h"
#include <stddef.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>

#include <stdio.h>
#include <assert.h>

typedef struct node node;

node *mk(char color, int key, node *l, node *r){
    node *n = malloc(sizeof(node));
    n->left = (node *)((uintptr_t)l << 1);
    if(color){
        n->left = (node *)((uintptr_t)n->left | 1);
    }
    n->right = r;
    n->key = key;
    return n;
}

void print_in_order(node *root){
    if(root == NULL) return;
    print_in_order(left(root));
    printf( "%d (%s)\n", root->key, (color(root) ? "RED" : "BLACK"));
    print_in_order(right(root));
}

void assert_and_print(bool should_be_tree, node *root){
    printf("=====\n");
    print_in_order(root);
    assert(is_red_black_tree(root) == should_be_tree);
}

void test_construction(){
    node *l = mk(RED, 1, NULL, NULL);
    node *r = mk(RED, 3, NULL, NULL);
    node *x = mk(BLACK, 2, l, r);

    assert(color(x) == BLACK);
    assert(x->key == 2);

    node *ll = left(x);
    assert(ll == l);
    assert(ll->key == 1);
    assert(left(ll) == NULL);
    assert(right(ll) == NULL);

    node *rr = right(x);
    assert(rr == r);
    assert(rr->key == 3);
    assert(left(rr) == NULL);
    assert(right(rr) == NULL);

    printf( "All construction tests passed!\n" );
}

void test_is(){
    assert_and_print(false, NULL);

    node *o = mk(BLACK, 99, NULL, NULL);
    assert_and_print(true, o);

    node *l = mk(RED, 1, NULL, NULL);
    node *r = mk(RED, 3, NULL, NULL);
    assert_and_print(false, r);

    node *x = mk(BLACK, 2, l, r);
    assert_and_print(true, x);


    node *n106 = mk(RED, 106, NULL, NULL);
    node *n107 = mk(BLACK, 107, n106, NULL);

    node *n140 = mk(RED, 140, NULL, NULL);
    node *n161 = mk(BLACK, 161, n140, NULL);
    node *n261 = mk(BLACK, 261, NULL, NULL);
    node *n166 = mk(RED, 166, n161, n261);

    node *n110 = mk(BLACK, 110, n107, n166);
    assert_and_print(true, n110);


    node *n26 = mk(BLACK, 26, NULL, NULL);
    node *n41 = mk(BLACK, 41, NULL, NULL);
    node *n31 = mk(BLACK, 31, n26, n41);

    node *n59 = mk(BLACK, 59, NULL, NULL);
    node *n97 = mk(BLACK, 97, NULL, NULL);
    node *n58 = mk(BLACK, 58, n59, n97);
    
    node *n53 = mk(BLACK, 53, n31, n58);
    assert_and_print(true, n53);

    node *n1 = mk(BLACK, 1, NULL, NULL);
    node *n4 = mk(BLACK, 4, NULL, NULL);
    node *n2 = mk(BLACK, 2, n1, n4);

    node *n8 = mk(BLACK, 8, NULL, NULL);
    node *n7 = mk(BLACK, 7, NULL, n8);

    node *n5 = mk(BLACK, 5, n2, n7);
    assert_and_print(false, n5);


    printf( "All is_red_black_tree tests passed!\n" );
}

int main(int argc, const char* argv[])
{
    printf( "\nStarting tests.\n\n" );

    test_construction();
    test_is();
    // node *n = mkNode(0, 4);
    // assert(color(n) == 0);
    // assert(left(n) == NULL);
    // free(n);
    // n = mkNode(1, 4);
    // assert(color(n) == 1);
    // assert(left(n) == NULL);
    // free(n);

    printf( "All tests passed!\n" );

}