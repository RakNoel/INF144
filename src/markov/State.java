package markov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * markov.State objects to hold directed links to and their
 * weight to other nodes. Also remembers its total
 * weight and name.
 *
 * @param <T>
 */
public class State<T> implements Iterable<State<T>> {
    private HashMap<State, Integer> link_weight;
    private ArrayList<State<T>> links;
    private int totalWeight;
    private String name;

    public State(T t) {
        this.link_weight = new HashMap<>();
        this.name = t.toString();
        this.links = new ArrayList<>();
        this.totalWeight = 0;
    }

    public void editWeight(State<T> state, int value) {
        if (!link_weight.containsKey(state))
            links.add(state);

        this.totalWeight += value;
        link_weight.merge(state, value, (x, y) -> x + y);
    }

    public int getTotalWeight() {
        return this.totalWeight;
    }

    public int getWeight(State state) {
        return link_weight.getOrDefault(state, 0);
    }

    @Override
    public Iterator<State<T>> iterator() {
        return this.links.iterator();
    }

    @Override
    public String toString() {
        return this.name;
    }
}