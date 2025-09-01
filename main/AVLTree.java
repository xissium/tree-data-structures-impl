import java.util.Comparator;

/**
 * AVLTree: A self-balancing Binary Search Tree (BST).
 * <p>
 * Properties:
 * <li>The height difference between left and right subtrees is at most 1.</li>
 *
 * @param <T> generic type (must be Comparable or use Comparator)
 */
public class AVLTree<T> extends BinarySearchTree<T> {
    /* ==================== Constructors ==================== */

    public AVLTree() {
    }

    public AVLTree(boolean allowDuplicates) {
        super(allowDuplicates);
    }

    public AVLTree(Comparator<T> comparator) {
        super(comparator);
    }

    public AVLTree(Comparator<T> comparator, boolean allowDuplicates) {
        super(comparator, allowDuplicates);
    }

    /* ==================== Utilities ==================== */

    private void updateHeight(TreeNode<T> node) {
        if (isNil(node)) return;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int height(TreeNode<T> node) {
        return isNil(node) ? 0 : node.height;
    }

    /* ==================== Rotations ==================== */

    /**
     * @param node root of subtree to be rotated left
     * @return new root of rebalanced subtree
     */
    private TreeNode<T> rotateLeft(TreeNode<T> node) {
        //    |                       |
        //    N                       S
        //   / \     l-rotate(N)     / \
        //  A   S    ==========>    N   C
        //     / \                 / \
        //    B   C               A   B
        TreeNode<T> successor = node.right;
        node.right = successor.left;
        successor.left = node;
        updateHeight(node);
        updateHeight(successor);
        updateSize(node);
        updateSize(successor);
        return successor;
    }

    /**
     * @param node root of subtree to be rotated right
     * @return new root of rebalanced subtree
     */
    private TreeNode<T> rotateRight(TreeNode<T> node) {
        //      |                   |
        //      N                   S
        //     / \   r-rotate(N)   / \
        //    S   C  ==========>  A   N
        //   / \                     / \
        //  A   B                   B   C
        TreeNode<T> successor = node.left;
        node.left = successor.right;
        successor.right = node;
        updateHeight(node);
        updateHeight(successor);
        updateSize(node);
        updateSize(successor);
        return successor;
    }

    /* ==================== Balancing ==================== */

    private int balanceFactor(TreeNode<T> node) {
        return height(node.left) - height(node.right);
    }

    private TreeNode<T> fixBalance(TreeNode<T> node) {
        if (isNil(node)) return node;
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                //  Left-Right Case
                //     |                  |
                //     C                  C
                //    /   l-rotate(A)    /
                //   A    ==========>   B
                //    \                /
                //     B              A
                node.left = rotateLeft(node.left);
            }
            //  Left-Left Case
            //       |
            //       C                 |
            //      /   r-rotate(C)    B
            //     B    ==========>   / \
            //    /                  A   C
            //   A
            node = rotateRight(node);
        } else if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                //  Right-Left Case
                //   |                 |
                //   A                 A
                //    \   r-rotate(C)   \
                //     C  ==========>    B
                //    /                   \
                //   B                     C
                node.right = rotateRight(node.right);
            }
            //  Right-Right Case
            //   |
            //   A                     |
            //    \     l-rotate(A)    B
            //     B    ==========>   / \
            //      \                A   C
            //       C
            node = rotateLeft(node);
        }
        return node;
    }

    /* ==================== Insertion & Deletion ==================== */

    @Override
    protected TreeNode<T> insert(TreeNode<T> node, T data) {
        node = super.insert(node, data);
        updateHeight(node);
        return fixBalance(node);
    }

    @Override
    protected TreeNode<T> delete(TreeNode<T> node, T data) {
        node = super.delete(node, data);
        updateHeight(node);
        return fixBalance(node);
    }
}
