/**
 * Wielowątkowo bezpieczne drzewo binarne (tylko klucze).
 */
public class BT<K extends Comparable<K>> {
    private BTNode<K> root;

    public BT() {
        this.root = null;
    }

    public synchronized void insert(K key) {
        root = insertRecursive(root, key);
    }

    private BTNode<K> insertRecursive(BTNode<K> node, K key) {
        if (node == null) return new BTNode<>(key);
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, key);
        }
        return node;
    }

    public synchronized boolean search(K key) {
        return searchRecursive(root, key) != null;
    }

    private BTNode<K> searchRecursive(BTNode<K> node, K key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        
        return (cmp < 0) ? searchRecursive(node.left, key) : searchRecursive(node.right, key);
    }

    public synchronized void delete(K key) {
        root = deleteRecursive(root, key);
    }

    private BTNode<K> deleteRecursive(BTNode<K> node, K key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRecursive(node.right, key);
        } else {
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            
            BTNode<K> successor = findMin(node.right);
            node.key = successor.key;
            node.right = deleteRecursive(node.right, successor.key);
        }
        return node;
    }

    private BTNode<K> findMin(BTNode<K> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Serializuje drzewo do formatu [Klucz[Lewy][Prawy]], np: [5[3[][]][8[][]]]
     */
    public synchronized String serialize() {
        if (root == null) return "[]";
        return serializeNode(root);
    }

    private String serializeNode(BTNode<K> node) {
        if (node == null) return "[]";
        return "[" + node.key.toString() + serializeNode(node.left) + serializeNode(node.right) + "]";
    }
}