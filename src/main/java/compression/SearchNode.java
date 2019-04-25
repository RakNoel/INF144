package compression;

/**
 * A class for search-nodes
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class SearchNode<T> implements Node<T> {
    private Node<T> left;
    private Node<T> right;
    private int value;

    SearchNode(Node<T> left, Node<T> right, int value) {
        this.left = left;
        this.right = right;
        this.value = value;
    }

    @Override
    public String getName() {
        return null; //Should not have a name!
    }

    @Override
    public int getTotalWeight() {
        return this.value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "," + right + ")";
    }

    @Override
    public int compareTo(Node<T> tNode) {
        return Integer.compare(this.value, tNode.getTotalWeight());
    }
}