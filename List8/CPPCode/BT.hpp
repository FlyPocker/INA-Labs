#ifndef BT_HPP
#define BT_HPP

#include "BTNode.hpp"
#include <string>
#include <sstream>

template <typename K>
class BT {
private:
    BTNode<K>* root;

    void clear(BTNode<K>* node) {
        if (node != nullptr) {
            clear(node->left);
            clear(node->right);
            delete node;
        }
    }

    BTNode<K>* insertRecursive(BTNode<K>* current, K key) {
        if (current == nullptr) return new BTNode<K>(key);
        
        if (key < current->key) {
            current->left = insertRecursive(current->left, key);
        } else if (key > current->key) {
            current->right = insertRecursive(current->right, key);
        }
        return current;
    }

    BTNode<K>* searchRecursive(BTNode<K>* current, K key) const {
        if (current == nullptr || current->key == key) return current;
        if (key < current->key) return searchRecursive(current->left, key);
        return searchRecursive(current->right, key);
    }

    BTNode<K>* findMin(BTNode<K>* node) {
        while (node->left != nullptr) node = node->left;
        return node;
    }

    BTNode<K>* deleteRecursive(BTNode<K>* current, K key) {
        if (current == nullptr) return nullptr;

        if (key < current->key) {
            current->left = deleteRecursive(current->left, key);
        } else if (key > current->key) {
            current->right = deleteRecursive(current->right, key);
        } else {
            if (current->left == nullptr) {
                BTNode<K>* temp = current->right;
                delete current;
                return temp;
            } else if (current->right == nullptr) {
                BTNode<K>* temp = current->left;
                delete current;
                return temp;
            }
            BTNode<K>* successor = findMin(current->right);
            current->key = successor->key;
            current->right = deleteRecursive(current->right, successor->key);
        }
        return current;
    }

    // Zmiana na kwadratowe nawiasy
    std::string serializeNode(BTNode<K>* node) const {
        if (node == nullptr) return "[]";
        std::stringstream ss;
        ss << "[" << node->key << serializeNode(node->left) << serializeNode(node->right) << "]";
        return ss.str();
    }

public:
    BT() : root(nullptr) {}
    ~BT() { clear(root); }

    void insert(K key) {
        root = insertRecursive(root, key);
    }

    bool search(K key) const {
        return searchRecursive(root, key) != nullptr;
    }

    void remove(K key) {
        root = deleteRecursive(root, key);
    }

    std::string serialize() const {
        if (root == nullptr) return "[]";
        return serializeNode(root);
    }
};

#endif // BT_HPP