
/**
 * Klasa reprezentująca pojedynczy węzeł drzewa BT.
 * @param <K> Typ klucza, który musi implementować interfejs Comparable.
 * @param <V> Typ przechowywanej wartości.
 */
public class BTNode<K extends Comparable<K>, V> {
    public K key;
    public V value;
    public BTNode<K, V> left;
    public BTNode<K, V> right;

    /**
     * Tworzy nowy węzeł drzewa z określonym kluczem i wartością.
     */
    public BTNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }
}