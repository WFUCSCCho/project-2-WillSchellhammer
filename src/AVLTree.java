/************************************************************************
 * @file: AVLTree.java
 * @description: AVL Balanced Binary Search Tree, useable for any "Comparable" object.
 * @author: Will S
 * @date: October 21, 2025
 ************************************************************************/

// AVLTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> {
    // The tree root.
    private AVLNode<AnyType> root;

    // Construct the tree.
    public AVLTree( ) {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x ) {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x ) {
        root = remove( x, root );
    }


    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    //Searches subtree with root t in pre-order for a match to x and removes and returns it, or returns null if no match is found
    private AVLNode<AnyType> remove(AnyType x, AVLNode<AnyType> t ) {
        if (t == null || !contains(x)) return null;
        else {
            return removeHelp(x, t);
        }
    }

    //Recursive helper method for remove()
    private AVLNode<AnyType> removeHelp(AnyType x, AVLNode<AnyType> t) {
        if (t == null) return null;
        if (t.compareTo(x) > 0)
            t.setLeft(removeHelp(x, t.getLeft()));
        else if (t.compareTo(x) < 0)
            t.setRight(removeHelp(x, t.getRight()));
        else {
            if (t.getLeft() == null) { //right child only, return right
                return t.getRight();
            } else if (t.getRight() == null) { //left child only, return left
                return t.getLeft();
            } else { //both children, set rt to the lowest value in the right, then delete the lowest value from its previous position
                AVLNode<AnyType> temp = findMin(t.getRight());
                t.setData(temp.data);
                t.setRight(removeHelp(temp.data, t.getRight()));
            }
        }
        return t;
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMin( root ).getData();
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMax( root ).getData();
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( AnyType x ) {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( ) {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( ) {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( ) {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AVLNode<AnyType> balance(AVLNode<AnyType> t ) {
        if (t == null) return t;
        AVLNode<AnyType> left = t.getLeft();
        AVLNode<AnyType> right = t.getRight();
        if (height(left) - height(right) > ALLOWED_IMBALANCE) {
            if (height(left.getLeft()) >= height(left.getRight())) {
                t = rotateWithLeftChild(t);
            }
            else {
                t = doubleWithLeftChild(t);
            }
        }
        else if (height(right) - height(left) > ALLOWED_IMBALANCE) {
            if (height(right.getRight()) >= height(right.getLeft())) {
                t = rotateWithRightChild(t);
            }
            else {
                t = doubleWithRightChild(t);
            }
        }
        t.height = Math.max(height(left), height(right)) + 1;
        return t;
    }

    public void checkBalance( ) {
        checkBalance( root );
    }

    private int checkBalance( AVLNode<AnyType> t ) {
        if( t == null )
            return -1;

        if( t != null ) {
            int hl = checkBalance( t.getLeft() );
            int hr = checkBalance( t.getRight() );
            if( Math.abs( height( t.getLeft() ) - height( t.getRight() ) ) > 1 ||
                    height( t.getLeft() ) != hl || height( t.getRight() ) != hr )
                System.out.println( "OOPS!!" );
        }

        return height( t );
    }


    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    //Inserts x at in-order position in subtree with root t, or does nothing if it already exists in the subtree
    private AVLNode<AnyType> insert(AnyType x, AVLNode<AnyType> t ) {
        if (t == null) {
            return new AVLNode<AnyType>(x);
        }

        int compareResult = x.compareTo(t.getData());

        if (compareResult < 0) {
            t.setLeft(insert(x, t.getLeft()));
        }
        else if (compareResult > 0) {
            t.setRight(insert(x, t.getRight()));
        }
        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    //Returns the lowest AVLNode in the subtree (the first left child found)
    private AVLNode<AnyType> findMin(AVLNode<AnyType> t ) {
        if (t.getLeft() != null) return findMin(t.getLeft());
        else return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    //Returns the highest AVLNode in the subtree (the first right child found)
    private AVLNode<AnyType> findMax(AVLNode<AnyType> t ) {
        if (t.getRight() != null) return findMax(t.getRight());
        else return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    //Searches subtree with root t in pre-order for a match to x and returns true, or returns false if no match is found
    private boolean contains( AnyType x, AVLNode<AnyType> t ) {
        if (t == null) return false;

        int compareResult = x.compareTo(t.getData());

        if (compareResult < 0) {
            return contains(x, t.getLeft());
        }
        else if (compareResult > 0) {
            return contains(x, t.getRight());
        }
        else return true;
    }

    /**
     * Internal method to print a subtree in (sorted) order.
     * @param t the node that roots the tree.
     */
    //Prints subtree with root t in in-order to command box
    private void printTree( AVLNode<AnyType> t ) {
        if (t == null) return;
        printHelp(t);
        System.out.println();
    }

    //Recursive helper method for print()
    private void printHelp(AVLNode<AnyType> t) {
        if (t.getLeft() != null) printHelp(t.getLeft());
        System.out.print(t.getData() + "\n");
        if (t.getRight() != null) printHelp(t.getRight());
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height( AVLNode<AnyType> t ) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AVLNode<AnyType> rotateWithLeftChild(AVLNode<AnyType> k2 ) {
        AVLNode<AnyType> rt = k2; //shallow copy
        AVLNode<AnyType> left = k2.getLeft(); //shallow copy
        rt.setLeft(left.getRight()); //set root's left child to its left child's right child
        left.setRight(rt); //set root's left child to root
        rt.height = Math.max( height(left), height(rt.getRight())) + 1; //find max height of either the left side or right side of root and add 1
        left.height = Math.max( height(left.getLeft()), height(rt)) + 1; //find max height of either the left side or right side of left and add 1
        return left;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AVLNode<AnyType> rotateWithRightChild(AVLNode<AnyType> k1 ) {
        AVLNode<AnyType> rt = k1; //shallow copy
        AVLNode<AnyType> right = k1.getRight(); //shallow copy
        rt.setRight(right.getLeft()); //set root's right child to its right child's left child
        right.setLeft(rt); //set root's right child to root
        rt.height = Math.max( height(right), height(rt.getLeft())) + 1; //find max height of either the left side or right side of root and add 1
        right.height = Math.max( height(right.getRight()), height(rt)) + 1; //find max height of either the left side or right side of left and add 1
        return right;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AVLNode<AnyType> doubleWithLeftChild(AVLNode<AnyType> k3 ) {
	    k3.setLeft(rotateWithRightChild(k3.getLeft()));
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AVLNode<AnyType> doubleWithRightChild(AVLNode<AnyType> k1 ) {
        k1.setRight(rotateWithLeftChild(k1.getRight()));
        return rotateWithRightChild(k1);
    }

    //*********************************************************************************************************
    // AVL NODE CLASS
    private static class AVLNode<AnyType extends Comparable<? super AnyType>> implements Comparable<AnyType> {
        // Constructors
        AVLNode(AnyType theData ) {
            this( theData, null, null );
        }

        AVLNode(AnyType theData, AVLNode<AnyType> lt, AVLNode<AnyType> rt ) {
            data  = theData;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        AnyType           data;      // The data in the node
        AVLNode<AnyType> left;         // Left child
        AVLNode<AnyType> right;        // Right child
        int               height;       // Height

        public void setData(AnyType data) {
            this.data = data;
        }
        public void setLeft(AVLNode<AnyType> left) {
            this.left = left;
        }
        public void setRight(AVLNode<AnyType> right) {
            this.right = right;
        }
        public AVLNode<AnyType> getLeft() {
            return left;
        }
        public AVLNode<AnyType> getRight() {
            return right;
        }
        public AnyType getData() {
            return data;
        }
        public boolean isLeaf() {
            return left == null && right == null;
        }

        //Compare a node to a node based on their data
        public int compareTo(AVLNode<AnyType> n) {
            return data.compareTo(n.data);
        }

        //Compare a node to data based on the node's data
        @Override
        public int compareTo(AnyType t) {
            return data.compareTo(t);
        }
    }
    //*********************************************************************************************************

    //*********************
    // Additional Methods

    //returns the count of all nodes in the AVL Tree
    public int size() {
        return sizeHelp(root);
    }

    //recursive helper method used by size()
    private int sizeHelp(AVLNode<AnyType> rt) {
        if (rt == null) return 0;
        if (rt.isLeaf()) return 1;
        return sizeHelp(rt.getLeft()) + sizeHelp(rt.getRight()) + 1;
    }

    //*********************

    //********************
    // Getters and Setters
    //********************

    public AVLNode<AnyType> getRoot() {
        return root;
    }

    public void setRoot(AVLNode<AnyType> root) {
        this.root = root;
    }
}
