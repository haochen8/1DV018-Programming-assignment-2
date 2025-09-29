package Lists;

import java.util.Iterator;

/**
 * A linked list implementation of a double-ended queue (deque).
 * @param <AnyType> the type of elements in the deque
 */
public class LinkedDeque<AnyType> implements Deque<AnyType> {
    private static class Node<AnyType> {
        public AnyType data;
        public Node<AnyType> next;
        public Node<AnyType> prev;

        public Node(AnyType d, Node<AnyType> p, Node<AnyType> n) {
            data = d;
            prev = p;
            next = n;
        }
    }
    private Node<AnyType> head, tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(AnyType x) {
        if (x == null) throw new IllegalArgumentException("Null values not allowed");
        head = new Node<>(x, null, head);
        if (tail == null) {
            tail = head;
        } else {
            head.next.prev = head;
        }
        size++;
    }

    public void addLast(AnyType x) {
      if (x == null) throw new IllegalArgumentException("Null values not allowed");
        tail = new Node<>(x, tail, null);
        if (head == null) {
            head = tail;
        } else {
            tail.prev.next = tail;
        }
        size++;
    }

    public AnyType removeFirst() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        AnyType data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return data;
    }

    public AnyType removeLast() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        AnyType data = tail.data;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return data;
    }

    public Iterator<AnyType> iterator() {
        return new Iterator<AnyType>() {
            private Node<AnyType> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public AnyType next() {
                if (!hasNext()) throw new IllegalStateException("No more elements");
                AnyType data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
