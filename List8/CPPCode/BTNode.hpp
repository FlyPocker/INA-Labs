#ifndef BTNODE_HPP
#define BTNODE_HPP

template <typename K>
struct BTNode {
    K key;
    BTNode* left;
    BTNode* right;

    BTNode(K k) : key(k), left(nullptr), right(nullptr) {}
};

#endif // BTNODE_HPP