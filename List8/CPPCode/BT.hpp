#ifndef BT_HPP
#define BT_HPP

#include "BTNode.hpp"
#include <string>
#include <mutex>
#include <vector>
#include <algorithm>
#include <sstream>

template <typename K, typename V>
class BT {
private:
    BTNode<K, V>* root;
    mutable std::mutex mtx; // Mutex do synchronizacji wątków sieciowych

    // Rekurencyjne czyszczenie pamięci (destruktor)
    void clear(BTNode<K, V>* node) {
        if (node != nullptr) {
            clear(node->left);
            clear(node->right);
            delete node;
        }
    }

    BTNode<K, V>* insertRecursive(BTNode<K, V>* current, K key, V value) {
        if (current == nullptr) return new BTNode<K, V>(key, value);
        if (key < current->key)       current->left = insertRecursive(current->left, key, value);
        else if (key > current->key)  current->right = insertRecursive(current->right, key, value);
        else                          current->value = value; // Aktualizacja wartości
        return current;
    }

    BTNode<K, V>* searchRecursive(BTNode<K, V>* current, K key) const {
        if (current == nullptr || current->key == key) return current;
        if (key < current->key) return searchRecursive(current->left, key);
        return searchRecursive(current->right, key);
    }

    BTNode<K, V>* findMin(BTNode<K, V>* node) {
        while (node->left != nullptr) node = node->left;
        return node;
    }

    BTNode<K, V>* deleteRecursive(BTNode<K, V>* current, K key) {
        if (current == nullptr) return nullptr;

        if (key < current->key) {
            current->left = deleteRecursive(current->left, key);
        } else if (key > current->key) {
            current->right = deleteRecursive(current->right, key);
        } else {
            if (current->left == nullptr) {
                BTNode<K, V>* temp = current->right;
                delete current;
                return temp;
            } else if (current->right == nullptr) {
                BTNode<K, V>* temp = current->left;
                delete current;
                return temp;
            }
            BTNode<K, V>* successor = findMin(current->right);
            current->key = successor->key;
            current->value = successor->value;
            current->right = deleteRecursive(current->right, successor->key);
        }
        return current;
    }

    int getMaxDepth(BTNode<K, V>* node, int depth) const {
        if (node == nullptr) return depth - 1;
        return std::max(getMaxDepth(node->left, depth + 1), getMaxDepth(node->right, depth + 1));
    }

    // Budowanie poziomów piramidy w porządku In-Order
    void fillLevelsInOrder(BTNode<K, V>* node, int depth, int& currentX, std::vector<std::string>& levels) const {
        if (node == nullptr) return;

        fillLevelsInOrder(node->left, depth + 1, currentX, levels);

        int spacing = 5; // Szerokość kolumny na ekranie
        int targetPosition = currentX * spacing;

        while (levels[depth].length() < static_cast<size_t>(targetPosition)) {
            levels[depth] += " ";
        }

        std::stringstream ss;
        ss << node->key;
        levels[depth] += ss.str();

        currentX++; // Przejście do kolejnej kolumny X

        fillLevelsInOrder(node->right, depth + 1, currentX, levels);
    }

public:
    BT() : root(nullptr) {}
    ~BT() { clear(root); } // Bezpieczne usunięcie całego drzewa z RAM-u

    void insert(K key, V value) {
        std::lock_guard<std::mutex> lock(mtx);
        root = insertRecursive(root, key, value);
    }

    bool search(K key, V& outValue) const {
        std::lock_guard<std::mutex> lock(mtx);
        BTNode<K, V>* node = searchRecursive(root, key);
        if (node != nullptr) {
            outValue = node->value;
            return true;
        }
        return false;
    }

    void remove(K key) {
        std::lock_guard<std::mutex> lock(mtx);
        root = deleteRecursive(root, key);
    }

    std::string toBottomUpPyramidString() const {
        std::lock_guard<std::mutex> lock(mtx);
        if (root == nullptr) return "Drzewo jest puste.";

        int maxDepth = getMaxDepth(root, 0);
        std::vector<std::string> levels(maxDepth + 1, "");

        int currentX = 0;
        fillLevelsInOrder(root, 0, currentX, levels);

        std::string result = "";
        // Składanie piramidy odwróconej (od liści w indeksie maxDepth do korzenia w indeksie 0)
        for (int i = maxDepth; i >= 0; i--) {
            result += levels[i] + "\n";
        }
        return result;
    }
};

#endif // BT_HPP