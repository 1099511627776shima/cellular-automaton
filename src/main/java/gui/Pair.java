package gui;

import cells.states.CellState;
import javafx.scene.paint.Color;

/**
 * Created by bzdeco on 09.12.16.
 */
public class Pair<K, V> {
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    private K key;
    private V value;
}
