enum NodeColor {RED, BLACK}

public class TreeNode<T> {
    T data;
    TreeNode<T> left, right, parent;
    int size, count;
    int height; // AVL Tree
    NodeColor color; // Red-Black Tree

    public TreeNode(T data) {
        this.data = data;
        this.size = this.count = this.height = 1;
        this.color = NodeColor.RED;
    }
}
