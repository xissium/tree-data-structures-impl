import java.util.Comparator;

/**
 * Red-Black Tree
 * <p>
 * Properties:
 * <p>
 * In addition to the requirements imposed on a binary search tree the following must be satisfied:
 * <li>Every node is either red or black.</li>
 * <li>The root is always black.</li>
 * <li>All leaf nodes (NIL) are black.</li>
 * <li>A red node does not have a red child.</li>
 * <li>Every path from a given node to any of its leaf nodes goes through the same number of black nodes.</li>
 *
 * @param <T> generic type (must be Comparable or use Comparator)
 */
public class RBTree<T> extends BinarySearchTree<T> {
    private final TreeNode<T> NIL;

    /* ==================== Constructors ==================== */

    public RBTree() {
        NIL = createNIL();
        initRoot();
    }

    public RBTree(boolean allowDuplicates) {
        super(allowDuplicates);
        NIL = createNIL();
        initRoot();
    }

    public RBTree(Comparator<T> comparator) {
        super(comparator);
        NIL = createNIL();
        initRoot();
    }

    public RBTree(Comparator<T> comparator, boolean allowDuplicates) {
        super(comparator, allowDuplicates);
        NIL = createNIL();
        initRoot();
    }

    /* ==================== Initialization ==================== */

    private TreeNode<T> createNIL() {
        TreeNode<T> nil = new TreeNode<>(null);
        nil.color = NodeColor.BLACK;
        nil.left = nil.right = nil.parent = nil;
        nil.count = nil.size = 0;
        return nil;
    }

    private void initRoot() {
        if (root == null) {
            root = NIL;
        }
    }

    @Override
    protected boolean isNil(TreeNode<T> node) {
        return node == NIL;
    }

    private TreeNode<T> createNode(T data) {
        TreeNode<T> node = new TreeNode<>(data);
        node.left = node.right = node.parent = NIL;
        return node;
    }

    /* ==================== Rotations ==================== */

    private void rotateLeft(TreeNode<T> node) {
        //    |                       |
        //    N                       S
        //   / \     l-rotate(N)     / \
        //  L   S    ==========>    N   R
        //     / \                 / \
        //    M   R               L   M
        TreeNode<T> successor = node.right;
        node.right = successor.left;
        if (!isNil(successor.left)) {
            successor.left.parent = node;
        }
        successor.parent = node.parent;
        if (isNil(node.parent)) {
            root = successor;
        } else if (node == node.parent.left) {
            node.parent.left = successor;
        } else {
            node.parent.right = successor;
        }
        successor.left = node;
        node.parent = successor;
        updateSize(node);
        updateSize(successor);
    }

    private void rotateRight(TreeNode<T> node) {
        TreeNode<T> successor = node.left;
        node.left = successor.right;
        if (!isNil(successor.right)) {
            successor.right.parent = node;
        }
        successor.parent = node.parent;
        if (isNil(node.parent)) {
            root = successor;
        } else if (node == node.parent.right) {
            node.parent.right = successor;
        } else {
            node.parent.left = successor;
        }
        successor.right = node;
        node.parent = successor;
        updateSize(node);
        updateSize(successor);
    }

    /**
     * Update size metadata along the path from {@code node} to root.
     *
     * @param node starting node
     */
    private void updateSizeUp(TreeNode<T> node) {
        while (!isNil(node)) {
            updateSize(node);
            node = node.parent;
        }
    }

    /* ==================== Insertion ==================== */

    @Override
    public void insert(T data) {
        TreeNode<T> parent = NIL;
        TreeNode<T> current = root;
        while (!isNil(current)) {
            parent = current;
            int cmp = compare(data, current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                if (allowDuplicates) {
                    current.count++;
                    updateSizeUp(current);
                }
                return;
            }
        }

        TreeNode<T> node = createNode(data);
        node.parent = parent;
        // Attach node to parent
        if (isNil(parent)) {
            root = node;
        } else if (compare(data, parent.data) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        updateSizeUp(node);
        insertFixup(node);
    }

    private void insertFixup(TreeNode<T> node) {
        while (node.parent.color == NodeColor.RED) {
            if (node.parent == node.parent.parent.left) {
                TreeNode<T> uncle = node.parent.parent.right;
                if (uncle.color == NodeColor.RED) {
                    // Case 1: both P and U are red
                    //      G             [G]
                    //     / \            / \
                    //   [P] [U]  ==>    P   U
                    //   /              /
                    // [N]            [N]
                    node.parent.color = NodeColor.BLACK;
                    uncle.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    node = node.parent.parent;
                } else { // P is red and U is black
                    if (node == node.parent.right) {
                        // Case 2: direction of N is different with direction of P
                        //    G                     G
                        //   / \   l-rotate(P)     / \
                        // [P]  U  ==========>   [N]  U
                        //   \                   /
                        //   [N]               [P]
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 3: direction of N is same as direction of P
                    //      G                   [P]            P
                    //     / \   r-rotate(G)    / \           / \
                    //   [P]  U  ==========>  [N]  G   ==>  [N] [G]
                    //   /                          \             \
                    // [N]                           U             U
                    node.parent.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                TreeNode<T> uncle = node.parent.parent.left;
                if (uncle.color == NodeColor.RED) {
                    node.parent.color = NodeColor.BLACK;
                    uncle.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = NodeColor.BLACK;
    }

    /* ==================== Deletion ==================== */

    /**
     * replace subtree rooted at {@code u} with subtree rooted at {@code v}.
     *
     * @param u node to replace
     * @param v replacement node
     */
    private void transplant(TreeNode<T> u, TreeNode<T> v) {
        if (isNil(u.parent)) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    @Override
    public void delete(T data) {
        // Target node
        TreeNode<T> z = search(data);
        if (isNil(z)) return;

        if (z.count > 1) {
            z.count--;
            updateSizeUp(z);
            return;
        }

        // BaseNode actually removed (or moved)
        TreeNode<T> y = z;
        // Track nodeColor of original removed node
        NodeColor yOriginalNodeColor = y.color;
        // BaseNode that replaces y
        TreeNode<T> x;

        if (isNil(z.left)) {
            x = z.right;
            transplant(z, z.right);
            updateSizeUp(x.parent);
        } else if (isNil(z.right)) {
            x = z.left;
            transplant(z, z.left);
            updateSizeUp(x.parent);
        } else {
            // successor
            y = findMin(z.right);
            yOriginalNodeColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
                updateSize(y);
            } else {
                transplant(y, y.right);
                updateSize(x.parent);
                // Attach z's right subtree to y
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            // Attach z's left subtree to y
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
            updateSizeUp(y);
        }

        // If a black node was removed, fix potential double-black violations
        if (yOriginalNodeColor == NodeColor.BLACK) {
            deleteFixup(x);
        }
    }

    private void deleteFixup(TreeNode<T> node) {
        while (!isNil(node.parent) && node.color == NodeColor.BLACK) {
            if (node == node.parent.left) {
                TreeNode<T> sibling = node.parent.right;
                if (sibling.color == NodeColor.RED) {
                    // Case 1: S is red
                    //    P                      S
                    //   / \    l-rotate(P)     / \
                    // |N| [S]  ==========>   [P]  D
                    //     / \                / \
                    //    C   D             |N|  C
                    sibling.color = NodeColor.BLACK;
                    node.parent.color = NodeColor.RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                // S must be black
                if (sibling.left.color == NodeColor.BLACK && sibling.right.color == NodeColor.BLACK) {
                    // Case 2: both C and D are black
                    //   {P}           {P}
                    //   / \           / \
                    // |N|  S   ==>  |N| [S]
                    //     / \           / \
                    //    C   D         C   D
                    sibling.color = NodeColor.RED;
                    node = node.parent;
                } else {
                    if (sibling.right.color == NodeColor.BLACK) {
                        // Case 3: C is red and D is black
                        //   {P}                    P
                        //   / \    r-rotate(S)    / \
                        // |N|  S   ==========>  |N|  C
                        //     / \                     \
                        //   [C]  D                    [S]
                        //                               \
                        //                                D
                        sibling.left.color = NodeColor.BLACK;
                        sibling.color = NodeColor.RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    // Case 4: D is red
                    //   {P}                   {S}
                    //   / \    l-rotate(P)    / \
                    // |N|  S   ==========>   P   D
                    //     / \               / \
                    //   {C} [D]            N   C
                    sibling.color = node.parent.color;
                    node.parent.color = NodeColor.BLACK;
                    sibling.right.color = NodeColor.BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                TreeNode<T> sibling = node.parent.left;
                if (sibling.color == NodeColor.RED) {
                    sibling.color = NodeColor.BLACK;
                    node.parent.color = NodeColor.RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling.left.color == NodeColor.BLACK && sibling.right.color == NodeColor.BLACK) {
                    sibling.color = NodeColor.RED;
                    node = node.parent;
                } else {
                    if (sibling.left.color == NodeColor.BLACK) {
                        sibling.right.color = NodeColor.BLACK;
                        sibling.color = NodeColor.RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = NodeColor.BLACK;
                    sibling.left.color = NodeColor.BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.color = NodeColor.BLACK;
    }
}
