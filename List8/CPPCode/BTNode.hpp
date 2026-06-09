#ifndef BTNODE_HPP
#define BTNODE_HPP

// W C++ zamiast generyków <K, V> stosujemy szablony (templates)
template <typename K, typename V>
struct BTNode {
    K key;
    V value;
    BTNode* left;
    BTNode* right;

    // Lista inicjalizacyjna konstruktora - bardzo wydajna w C++
    BTNode(K k, V v) : key(k), value(v), left(nullptr), right(nullptr) {}
};

#endif // BTNODE_HPP