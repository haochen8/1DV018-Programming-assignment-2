package Binary;

import java.util.*;

/**
 * A simple binary search tree implementation.
 * @param <AnyType> the type of elements in the tree, must be comparable
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> implements Iterable<AnyType> {
    private static class Node<AnyType> {
        public AnyType data;
        public Node<AnyType> left;
        public Node<AnyType> right;

        public Node(AnyType d, Node<AnyType> l, Node<AnyType> r) {
            data = d;
            left = l;
            right = r;
        }
    }

    private Node<AnyType> root;
    private int size;

    public int size() {
        return size;
    }

    public int height() {
        // Height of an empty tree is -1 by convention (only root is 0)
        return height(root);
    }

    private int height(Node<AnyType> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public void add(AnyType x) {
        if (x == null) throw new IllegalArgumentException("Null values not allowed");
        root = addHelper(root, x, 0);
    }

    private Node<AnyType> addHelper(Node<AnyType> node, AnyType x, int currentHeight) {
        if (node == null) {
            size++;
            return new Node<>(x, null, null);
        }
        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            node.left = addHelper(node.left, x, currentHeight + 1);
        } else if (cmp > 0) {
            node.right = addHelper(node.right, x, currentHeight + 1);
        }
        return node;
    }

    public boolean remove(AnyType x) {
        if (x == null) throw new IllegalArgumentException("Null values not allowed");
        int initialSize = size;
        root = removeHelper(root, x);
        return size < initialSize;
    }

    private Node<AnyType> removeHelper(Node<AnyType> node, AnyType x) {
        if (node == null) return null;
        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            node.left = removeHelper(node.left, x);
        } else if (cmp > 0) {
            node.right = removeHelper(node.right, x);
        } else {
            // Found node to remove
            if (node.left == null && node.right == null) {
                size--;
                return null;
            } else if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            } else {
                // Node with two children: Get the inorder successor (smallest in the right subtree)
                Node<AnyType> minNode = findMin(node.right);
                node.data = minNode.data;
                node.right = removeHelper(node.right, minNode.data); // Size
                
            }
        }
        return node;
    }

    public void removeKthLargest (int k) {
        if (k < 1 || k > size) throw new IllegalArgumentException("k is out of bounds");
        List<AnyType> elements = new ArrayList<>();
        inOrder(root, elements);
        AnyType kthLargest = elements.get(size - k);
        remove(kthLargest);
    }

    private Node<AnyType> findMin(Node<AnyType> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean contains(AnyType x) {
        if (x == null) throw new IllegalArgumentException("Null values not allowed");
        return containsHelper(root, x);
    }

    private boolean containsHelper(Node<AnyType> node, AnyType x) {
        if (node == null) return false;
        int cmp = x.compareTo(node.data);
        if (cmp < 0) return containsHelper(node.left, x);
        if (cmp > 0) return containsHelper(node.right, x);
        return true;
    }

    @Override
    public Iterator<AnyType> iterator() {
        List<AnyType> elements = new ArrayList<>();
        inOrder(root, elements);
        return elements.iterator();
    }

    
    private void inOrder(Node<AnyType> node, List<AnyType> elements) {
      if (node != null) {
        inOrder(node.left, elements);
        elements.add(node.data);
        inOrder(node.right, elements);
      }
    }

    private void preOrder(Node<AnyType> node, List<AnyType> elements) {
        if (node != null) {
            elements.add(node.data);
            preOrder(node.left, elements);
            preOrder(node.right, elements);
        }
    }

    private void postOrder(Node<AnyType> node, List<AnyType> elements) {
        if (node != null) {
            postOrder(node.left, elements);
            postOrder(node.right, elements);
            elements.add(node.data);
        }
    }

    public Iterable<AnyType> inOrder() {
        List<AnyType> elements = new ArrayList<>();
        inOrder(root, elements);
        return elements;
    }

    public Iterable<AnyType> preOrder() {
        List<AnyType> elements = new ArrayList<>();
        preOrder(root, elements);
        return elements;
    }

    public Iterable<AnyType> postOrder() {
        List<AnyType> elements = new ArrayList<>();
        postOrder(root, elements);
        return elements;
    }
}
