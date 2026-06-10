public class BT<K extends Comparable<K>, V> {
    private BTNode<K, V> root;

    public BT() {
        this.root = null;
    }

    public synchronized void insert(K key, V value) {
        root = insertRecursive(root, key, value);
    }

    private BTNode<K, V> insertRecursive(BTNode<K, V> current, K key, V value) {
        if (current == null) {
            return new BTNode<>(key, value);
        }
        int cmp = key.compareTo(current.key);
        if (cmp < 0) {
            current.left = insertRecursive(current.left, key, value);
        } else if (cmp > 0) {
            current.right = insertRecursive(current.right, key, value);
        } else {
            current.value = value;
        }
        return current;
    }

    public synchronized V search(K key) {
        BTNode<K, V> node = searchRecursive(root, key);
        return (node != null) ? node.value : null;
    }

    private BTNode<K, V> searchRecursive(BTNode<K, V> current, K key) {
        if (current == null) return null;
        int cmp = key.compareTo(current.key);
        if (cmp == 0) return current;
        return (cmp < 0) ? searchRecursive(current.left, key) : searchRecursive(current.right, key);
    }

    public synchronized void delete(K key) {
        root = deleteRecursive(root, key);
    }

    private BTNode<K, V> deleteRecursive(BTNode<K, V> current, K key) {
        if (current == null) return null;
        int cmp = key.compareTo(current.key);
        if (cmp < 0) {
            current.left = deleteRecursive(current.left, key);
        } else if (cmp > 0) {
            current.right = deleteRecursive(current.right, key);
        } else {
            if (current.left == null) return current.right;
            else if (current.right == null) return current.left;
            BTNode<K, V> successor = findMin(current.right);
            current.key = successor.key;
            current.value = successor.value;
            current.right = deleteRecursive(current.right, successor.key);
        }
        return current;
    }

    private BTNode<K, V> findMin(BTNode<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public synchronized String toReadableString() {
        if (root == null) return "Drzewo jest puste.";
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(root.key).append(":").append(root.value).append("]\n");
        printTopDownRecursive(root.left, "L: ", "", root.right == null, sb);
        printTopDownRecursive(root.right, "R: ", "", true, sb);
        return sb.toString();
    }

    private void printTopDownRecursive(BTNode<K, V> node, String branchIndicator, String indent, boolean isLastChild, StringBuilder sb) {
        if (node == null) return;
        sb.append(indent).append(isLastChild ? "\\-- " : "+-- ").append(branchIndicator).append("[").append(node.key).append(":").append(node.value).append("]\n");
        String nextIndent = indent + (isLastChild ? "    " : "|   ");
        printTopDownRecursive(node.left, "L: ", nextIndent, node.right == null, sb);
        printTopDownRecursive(node.right, "R: ", nextIndent, true, sb);
    }
}