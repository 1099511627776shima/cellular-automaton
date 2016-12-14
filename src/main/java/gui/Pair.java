package gui;

/**
 * This is a class allowing to keep pairs of related objects.
 *
 * @see gui.StatePicker
 */
public class Pair<K, V> {
    /**
     * Sole constructor of this class creating a pair from given objects.
     * @param key the object that will be used as key of the pair (similarly to single pair in {@link java.util.Map}
     * @param value the object that will be used as the value related to the key object
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key from the pair.
     *
     * @return the object representing key of the pair
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value from the pair.
     *
     * @return the object representing the value of the pair
     */
    public V getValue() {
        return value;
    }

    private K key;
    private V value;
}
