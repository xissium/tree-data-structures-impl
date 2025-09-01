import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class AbstractBinaryTree<T> {
    protected TreeNode<T> root;
    protected final boolean allowDuplicates;

    public AbstractBinaryTree() {
        this(false);
    }

    public AbstractBinaryTree(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    protected boolean isNil(TreeNode<T> node) {
        return node == null;
    }

    public List<T> inOrderTraversal() {
        List<T> list = new ArrayList<>();
        inOrderTraversal(root, list);
        return list;
    }

    private void inOrderTraversal(TreeNode<T> node, List<T> list) {
        if (isNil(node)) return;
        inOrderTraversal(node.left, list);
        for (int i = 0; i < node.count; i++) {
            list.add(node.data);
        }
        inOrderTraversal(node.right, list);
    }

    public List<T> preOrderTraversal() {
        List<T> list = new ArrayList<>();
        preOrderTraversal(root, list);
        return list;
    }

    private void preOrderTraversal(TreeNode<T> node, List<T> list) {
        if (isNil(node)) return;
        for (int i = 0; i < node.count; i++) {
            list.add(node.data);
        }
        preOrderTraversal(node.left, list);
        preOrderTraversal(node.right, list);
    }

    public List<T> postOrderTraversal() {
        List<T> list = new ArrayList<>();
        postOrderTraversal(root, list);
        return list;
    }

    private void postOrderTraversal(TreeNode<T> node, List<T> list) {
        if (isNil(node)) return;
        postOrderTraversal(node.left, list);
        postOrderTraversal(node.right, list);
        for (int i = 0; i < node.count; i++) {
            list.add(node.data);
        }
    }

    public List<T> levelOrderTraversal() {
        List<T> list = new ArrayList<>();
        if (isNil(root)) return list;
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode<T> node = queue.poll();
            for (int i = 0; i < node.count; i++) {
                list.add(node.data);
            }
            if (!isNil(node.left)) queue.add(node.left);
            if (!isNil(node.right)) queue.add(node.right);
        }
        return list;
    }
}
