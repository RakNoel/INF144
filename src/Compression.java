import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * TODO: Describe class
 *
 * @author RakNoel
 * @version 1.0
 * @since 20.03.18
 */
public class Compression {
    public static void main(String[] args) {

    }
}

class Huffman {

    private static PriorityQueue<Node<String>> getFrequencyQueue(String text) {
        MarkovModel<String> d0 = Markov.getOrder(text, 0); //Zeroth markov = freq analysis
        PriorityQueue<Node<String>> treeQueue = new PriorityQueue<>();
        d0.iterator().forEachRemaining(s -> treeQueue.add(new LeafNode<>(s, d0.getNode(s).getWeight(d0.getNode(s)))));

        return treeQueue;
    }

    private static SearchNode<String> buildTreeFrom(PriorityQueue<Node<String>> treeQueue) {
        while (treeQueue.size() > 1) {
            Node<String> left = treeQueue.poll();
            Node<String> right = treeQueue.poll();
            treeQueue.add(new SearchNode<>(left, right, left.getTotalWeight() + right.getTotalWeight()));
        }

        Node x = treeQueue.poll();
        return (x instanceof SearchNode) ? (SearchNode<String>) x : null;
    }

    private static HashMap<String, String> createCompresser(String original) {
        HashMap<String, String> compresser = new HashMap<>();
        treeBFS(buildTreeFrom(getFrequencyQueue(original)), "", compresser);
        return compresser;
    }

    private static void treeBFS(Node<String> searchRoot, String path, HashMap<String, String> compresser) {
        if (searchRoot instanceof LeafNode) {
            compresser.put(searchRoot.getName(), path);
            return;
        }

        treeBFS(((SearchNode<String>) searchRoot).getLeft(), path + "1", compresser);
        treeBFS(((SearchNode<String>) searchRoot).getRight(), path + "0", compresser);
    }

    public static String compressText(String original) {
        HashMap<String, String> compresser = createCompresser(original);

        StringBuilder bdr = new StringBuilder();

        for (char ch : original.toCharArray())
            bdr.append(compresser.get(ch + ""));

        return buildTreeFrom(getFrequencyQueue(original)).toString() + System.lineSeparator() + bdr.toString();
    }
}

class LZW {

}

class SearchNode<T> implements Node<T> {
    private Node<T> left;
    private Node<T> right;
    private int value;

    public SearchNode(Node left, Node right, int value) {
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

class LeafNode<T> implements Node<T> {
    private String name;
    private int value;

    public LeafNode(String name, int value) {
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
        return "(" + name + ")";
    }

    @Override
    public int compareTo(Node<T> tNode) {
        return Integer.compare(this.value, tNode.getTotalWeight());
    }
}