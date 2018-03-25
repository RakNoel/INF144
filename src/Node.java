/**
 * TODO: Describe class
 *
 * @author RakNoel
 * @version 1.0
 * @since 25.03.18
 */
public interface Node<T> extends Comparable<Node<T>> {
    String getName();

    int getTotalWeight();

    String toString();
}
