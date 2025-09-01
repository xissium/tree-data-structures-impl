import java.util.Comparator;

/**
 * Binary Search Tree
 *
 * @param <T> generic type (must be Comparable or use Comparator)
 */
public class BinarySearchTree<T> extends AbstractBinaryTree<T> {
    protected final Comparator<T> comparator;

    /* ==================== Constructors ==================== */

    public BinarySearchTree() {
        this(null, false);
    }

    public BinarySearchTree(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    public BinarySearchTree(Comparator<T> comparator) {
        this(comparator, false);
    }

    public BinarySearchTree(Comparator<T> comparator, boolean allowDuplicates) {
        super(allowDuplicates);
        this.comparator = comparator;
    }

    /* ==================== Utilities ==================== */

    /**
     * Compare two values using either:
     * <li>the provided comparator</li>
     * <li>or natural ordering (Comparable)</li>
     */
    @SuppressWarnings("unchecked")
    protected int compare(T a, T b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<T>) a).compareTo(b);
    }

    protected void updateSize(TreeNode<T> node) {
        if (isNil(node)) return;
        node.size = node.count + size(node.left) + size(node.right);
    }

    protected int size(TreeNode<T> node) {
        return isNil(node) ? 0 : node.size;
    }

    public int size() {
        return size(root);
    }

    /* ==================== Insertion ==================== */

    public void insert(T data) {
        root = insert(root, data);
    }

    protected TreeNode<T> insert(TreeNode<T> node, T data) {
        if (isNil(node)) return new TreeNode<>(data);
        int cmp = compare(data, node.data);
        if (cmp < 0) {
            node.left = insert(node.left, data);
        } else if (cmp > 0) {
            node.right = insert(node.right, data);
        } else if (allowDuplicates) {
            node.count++;
        }
        updateSize(node);
        return node;
    }

    /* ==================== Searching ==================== */

    public TreeNode<T> search(T data) {
        return search(root, data);
    }

    private TreeNode<T> search(TreeNode<T> node, T data) {
        if (isNil(node)) return node;
        int cmp = compare(data, node.data);
        if (cmp == 0) return node;
        return cmp < 0 ? search(node.left, data) : search(node.right, data);
    }

    /**
     * Find minimum node in a subtree (leftmost node).
     */
    protected TreeNode<T> findMin(TreeNode<T> node) {
        if (isNil(node)) return node;
        while (!isNil(node.left)) node = node.left;
        return node;
    }

    /* ==================== Deletion ==================== */

    public void delete(T data) {
        root = delete(root, data);
    }

    protected TreeNode<T> delete(TreeNode<T> node, T data) {
        if (isNil(node)) return node;
        int cmp = compare(data, node.data);
        if (cmp < 0) {
            node.left = delete(node.left, data);
        } else if (cmp > 0) {
            node.right = delete(node.right, data);
        } else {
            if (node.count > 1) {
                node.count--;
            } else if (isNil(node.left)) {
                node = node.right;
            } else if (isNil(node.right)) {
                node = node.left;
            } else {
                TreeNode<T> successor = findMin(node.right);
                node.data = successor.data;
                node.count = successor.count;
                successor.count = 1;
                node.right = delete(node.right, successor.data);
            }
        }
        updateSize(node);
        return node;
    }

    /* ==================== Rank & K-th ==================== */

    /**
     * Get the rank of a value (1-based).
     *
     * @param data the value
     * @return rank (0 if not found)
     */
    public int rank(T data) {
        return rank(root, data);
    }

    private int rank(TreeNode<T> node, T data) {
        if (isNil(node)) return 0;
        int cmp = compare(data, node.data);
        if (cmp == 0) return size(node.left) + 1;
        if (cmp < 0) return rank(node.left, data);
        return rank(node.right, data) + size(node.left) + node.count;
    }

    /**
     * Get the k-th smallest value in the BST (1-based).
     *
     * @param k position
     * @return value or null if k is invalid
     */
    public T select(int k) {
        if (k <= 0 || k > size(root)) return null;
        return select(root, k);
    }

    private T select(TreeNode<T> node, int k) {
        if (isNil(node)) return null;
        int leftSize = size(node.left);
        if (k <= leftSize) return select(node.left, k);
        if (k <= leftSize + node.count) return node.data;
        return select(node.right, k - leftSize - node.count);
    }
}
