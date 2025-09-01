import java.util.*;

public class TestTree {
    private static void testTree(BinarySearchTree<Integer> tree) {
        List<Integer> values = Arrays.asList(17, 18, 23, 34, 27, 15, 9, 6, 25, 13, 10, 37);
        values.forEach(tree::insert);
        System.out.println(tree.inOrderTraversal());
        List<Integer> removeValues = Arrays.asList(18, 25, 15, 6, 13, 37, 27, 17, 34, 9, 10);
        removeValues.forEach(value -> {
            tree.delete(value);
            System.out.println("Deleted " + value + ": " + tree.inOrderTraversal());
        });
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        AVLTree<Integer> avlTree = new AVLTree<>();
        RBTree<Integer> rbTree = new RBTree<>();

        System.out.println("==================== Test BST ====================");
        testTree(bst);
        System.out.println();

        System.out.println("==================== Test AVLTree ====================");
        testTree(avlTree);
        System.out.println();

        System.out.println("==================== Test RBTree ====================");
        testTree(rbTree);
        System.out.println();
    }
}
