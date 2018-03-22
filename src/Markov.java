import java.io.File;
import java.util.*;

/**
 * Class with functions made to represent the Markov model.
 *
 * Main point of this class is to generate random strings
 * from a textfile with ordered approximations. Hopefully
 * getting humanly readable text/words out.
 *
 * @author RakNoel
 * @version 1.0
 * @since 20.03.18
 */
public class Markov {

    /**
     * Main method only meant to solve the assignement task itself
     * and to test output
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String text = new Scanner(new File("res/askeladden.txt")).nextLine().toLowerCase();

        MarkovModel<String> d0 = getOrder(text, 0);
        System.out.print("Zeroth order:: ");
        for (int i = 0; i < 30; i++) {
            System.out.print(d0.getRandomFreqNode());
        }

        System.out.println();
        System.out.print("First order:: ");
        System.out.println(generateFromText(text, 1));
        System.out.print("Second order:: ");
        System.out.println(generateFromText(text, 2));
        System.out.print("Third order:: ");
        System.out.println(generateFromText(text, 3));


    }

    /**
     * This id the generation method, which uses a markov-model to
     * weighted-random generate strings of length 30.
     *
     * @param text  the text from which to model
     * @param order the order of how large each node should be
     * @return the generated string
     */
    public static String generateFromText(String text, int order) {
        MarkovModel<String> d0 = getOrder(text, order);
        StringBuilder bldr = new StringBuilder();
        Node<String> startnode = d0.getRandomFreqNode();

        String hold = startnode.toString();
        for (int i = 0; i < 30; i++) {
            String x = d0.getNextNode(hold).toString();
            bldr.append(x);
            hold = hold.substring(1) + x;
        }

        return bldr.toString();
    }

    /**
     * Method to create a Markov-model-graph of given order for a text
     *
     * @param input the text from which to model
     * @param order the order of how large each node should be
     * @return A markov-model
     */
    public static MarkovModel<String> getOrder(String input, int order) {
        MarkovModel<String> holder = new MarkovModel<>();

        char[] text = input.toCharArray();
        for (int i = 0; i < text.length - order; i++) {
            StringBuilder key = new StringBuilder("" + text[i]);
            String value = "" + text[i + order];

            for (int k = 1; k < order; k++)
                key.append(text[i + k]);

            holder.addInstance(key.toString(), value);
        }
        return holder;
    }
}

/**
 * Node objects to hold directed links to and their
 * weight to other nodes. Also remembers its total
 * weight and name.
 *
 * @param <T>
 */
class Node<T> implements Iterable<Node<T>> {
    private HashMap<Node, Integer> link_weight;
    private ArrayList<Node<T>> links;
    private int totalWeight;
    private String name;

    public Node(T t) {
        this.link_weight = new HashMap<>();
        this.name = t.toString();
        this.links = new ArrayList<>();
        this.totalWeight = 0;
    }

    public void editWeight(Node<T> node, int value) {
        if (!link_weight.containsKey(node))
            links.add(node);

        this.totalWeight += value;
        link_weight.merge(node, value, (x, y) -> x + y);
    }

    public int getTotalWeight() {
        return this.totalWeight;
    }

    public int getWeight(Node node) {
        return link_weight.getOrDefault(node, 0);
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return this.links.iterator();
    }

    @Override
    public String toString() {
        return this.name;
    }
}

/**
 * Object to hold the nodes representing a markov model
 * in a collected array / hashmap, with methods
 * to use the graph.
 *
 * @param <T> The type in which to store in the model. Usually String.
 */
class MarkovModel<T> implements Iterable<T> {

    private ArrayList<T> elems;
    private HashMap<T, Node<T>> elemToNode;
    private int totalWeight;

    public MarkovModel() {
        this.elems = new ArrayList<>();
        this.elemToNode = new HashMap<>();
        this.totalWeight = 0;
    }

    public void addInstance(T t1, T t2) {
        if (!elems.contains(t1))
            elems.add(t1);
        this.totalWeight++;
        elemToNode.computeIfAbsent(t1, Node::new).editWeight(elemToNode.computeIfAbsent(t2, Node::new), 1);
    }

    public Node<T> getNode(T t) {
        return this.elemToNode.get(t);
    }

    public Node<T> getRandomNode() {
        return this.getNode(this.elems.get(new Random().nextInt(this.elems.size())));
    }

    public Node<T> getRandomFreqNode() {
        int indexer = new Random().nextInt(this.totalWeight + 1);

        for (T n : this) {
            if (indexer <= this.getNode(n).getTotalWeight())
                return this.getNode(n);
            else
                indexer -= this.getNode(n).getTotalWeight();
        }
        //SHOULD NEVER HAPPEN
        return null;
    }

    public Node<T> getNextNode(T element) {
        Node<T> node = this.getNode(element);
        int indexer = new Random().nextInt(node.getTotalWeight() + 1);

        for (Node<T> n : node) {
            if (indexer <= node.getWeight(n))
                return n;
            else
                indexer -= node.getWeight(n);
        }
        //SHOULD NEVER HAPPEN
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return elems.iterator();
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();

        for (T s : this) {
            bldr.append(s);
            bldr.append(" -> ");
            for (Node s1 : this.getNode(s))
                bldr.append("(" + s1 + ", " + this.getNode(s).getWeight(s1) + ") ");

            bldr.append(System.lineSeparator());
        }

        return bldr.toString();
    }
}