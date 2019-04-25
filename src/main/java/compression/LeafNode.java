package compression;

/**
 * A class for leafnodes
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class LeafNode<T> implements Node<T> {
    private String name;
    private int value;

    LeafNode(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getTotalWeight() {
        return this.value;
    }

    @Override
    public String toString() {
        return "(" + this.name + "," + this.value + ")";
    }

    @Override
    public Node<T> getLeft() {
        return null;
    }

    @Override
    public Node<T> getRight() {
        return null;
    }

    @Override
    public int compareTo(Node<T> tNode) {
        return Integer.compare(this.value, tNode.getTotalWeight());
    }
}