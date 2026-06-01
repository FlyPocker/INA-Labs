/**
 * Zsynchronizowana struktura drzewa poszukiwań binarnych (BST).
 * @param <K> Typ klucza (porównywalny).
 * @param <V> Typ wartości.
 */
public class BT<K extends Comparable<K>, V> {
    private BTNode<K, V> root;

    public BT() {
        this.root = null;
    }

    /**
     * Wstawia parę klucz-wartość do drzewa. Jeśli klucz istnieje, aktualizuje wartość.
     */
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
            current.value = value; // Aktualizacja wartości dla istniejącego klucza
        }
        return current;
    }

    /**
     * Wyszukuje wartość przypisaną do podanego klucza.
     * @return Wartość lub null, jeśli klucz nie istnieje.
     */
    public synchronized V search(K key) {
        BTNode<K, V> node = searchRecursive(root, key);
        return (node != null) ? node.value : null;
    }

    private BTNode<K, V> searchRecursive(BTNode<K, V> current, K key) {
        if (current == null) {
            return null;
        }

        int cmp = key.compareTo(current.key);
        if (cmp == 0) {
            return current;
        }
        return (cmp < 0) 
            ? searchRecursive(current.left, key) 
            : searchRecursive(current.right, key);
    }

    /**
     * Usuwa węzeł o podanym kluczu z drzewa.
     */
    public synchronized void delete(K key) {
        root = deleteRecursive(root, key);
    }

    private BTNode<K, V> deleteRecursive(BTNode<K, V> current, K key) {
        if (current == null) {
            return null;
        }

        int cmp = key.compareTo(current.key);
        if (cmp < 0) {
            current.left = deleteRecursive(current.left, key);
        } else if (cmp > 0) {
            current.right = deleteRecursive(current.right, key);
        } else {
            // Przypadek 1 i 2: Węzeł ma jedno dziecko lub brak dzieci
            if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            }

            // Przypadek 3: Węzeł ma dwoje dzieci. Szukamy najmniejszego w prawym poddrzewie.
            BTNode<K, V> successor = findMin(current.right);
            current.key = successor.key;
            current.value = successor.value;
            current.right = deleteRecursive(current.right, successor.key);
        }
        return current;
    }

    private BTNode<K, V> findMin(BTNode<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Zwraca tekstową reprezentację drzewa w porządku In-Order.
     * Przydatne do przesłania aktualnego stanu drzewa przez sieć.
     */
    public synchronized String toInOrderString() {
        StringBuilder sb = new StringBuilder();
        inOrderRecursive(root, sb);
        return sb.toString().trim();
    }

    private void inOrderRecursive(BTNode<K, V> node, StringBuilder sb) {
        if (node != null) {
            inOrderRecursive(node.left, sb);
            sb.append("[").append(node.key).append(":").append(node.value).append("] ");
            inOrderRecursive(node.right, sb);
        }
    }

    /**
     * Zwraca "graficzną" reprezentację drzewa obróconą o 90 stopni.
     */
    public synchronized String toPrettyString() {
        if (root == null) {
            return "Drzewo jest puste.";
        }
        StringBuilder sb = new StringBuilder();
        // Zaczynamy od korzenia na poziomie 0
        prettyPrintRecursive(root, 0, sb); 
        return sb.toString();
    }

    private void prettyPrintRecursive(BTNode<K, V> node, int level, StringBuilder sb) {
        if (node != null) {
            // 1. Najpierw idziemy w prawo (góra ekranu)
            prettyPrintRecursive(node.right, level + 1, sb);
            
            // 2. Dodajemy wcięcia dla aktualnego poziomu (4 spacje na każdy poziom)
            for (int i = 0; i < level; i++) {
                sb.append("    "); 
            }
            // Dodajemy sam węzeł i przejście do nowej linii (\n)
            sb.append("[").append(node.key).append(":").append(node.value).append("]\n");
            
            // 3. Potem idziemy w lewo (dół ekranu)
            prettyPrintRecursive(node.left, level + 1, sb); 
        }
    }
}