/**
 * Reprezentuje pojedynczy węzeł w drzewie. Przechowuje tylko klucz.
 */
public class BTNode<K extends Comparable<K>> {
    public K key;
    public BTNode<K> left;
    public BTNode<K> right;

    public BTNode(K key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }
}