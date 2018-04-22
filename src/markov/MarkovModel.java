package markov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Object to hold the nodes representing a markov model
 * in a collected array / hashmap, with methods
 * to use the graph.
 *
 * @param <T> The type in which to store in the model. Usually String.
 */
public class MarkovModel<T> implements Iterable<T> {

    private ArrayList<T> elems;
    private HashMap<T, State<T>> elemToNode;
    private int totalWeight;

    MarkovModel() {
        this.elems = new ArrayList<>();
        this.elemToNode = new HashMap<>();
        this.totalWeight = 0;
    }

    public void addInstance(T t1, T t2) {
        if (!elems.contains(t1))
            elems.add(t1);
        this.totalWeight++;
        elemToNode.computeIfAbsent(t1, State::new).editWeight(elemToNode.computeIfAbsent(t2, State::new), 1);
    }

    public State<T> getNode(T t) {
        return this.elemToNode.get(t);
    }

    public State<T> getRandomFreqNode() {
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

    public State<T> getNextNode(T element) {

        State<T> state = this.getNode(element);
        int indexer = new Random().nextInt(state.getTotalWeight() + 1);

        for (State<T> n : state) {
            if (indexer <= state.getWeight(n))
                return n;
            else
                indexer -= state.getWeight(n);
        }

        //If end of line return random node
        return getRandomFreqNode();
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
            for (State s1 : this.getNode(s)) {
                bldr.append(" (");
                bldr.append(s1);
                bldr.append(", ");
                bldr.append(this.getNode(s).getWeight(s1));
                bldr.append(") ");
            }
            bldr.append(System.lineSeparator());
        }

        return bldr.toString();
    }
}