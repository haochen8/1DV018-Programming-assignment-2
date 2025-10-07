package Lists;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * A simple linked list implementation.
 * 
 * @param <AnyType> the type of elements in the list
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType> {
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

  private Node<AnyType> head;
  private Node<AnyType> tail;
  private int size;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void addFirst(AnyType x) {
    head = new Node<>(x, null, head);
    if (tail == null) {
      tail = head;
    } else {
      head.next.prev = head;
    }
    size++;
  }

  public void addLast(AnyType x) {
    tail = new Node<>(x, tail, null);
    if (head == null) {
      head = tail;
    } else {
      tail.prev.next = tail;
    }
    size++;
  }

  public AnyType removeFirst() {
    if (isEmpty()) {
      throw new IllegalStateException("List is empty");
    }
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
    if (isEmpty()) {
      throw new IllegalStateException("List is empty");
    }
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

  public AnyType get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    Node<AnyType> current = head;
    for (int i = 0; i < index; i++) {
      current = current.next;
    }
    return current.data;
  }

  public boolean removeFirstMatch(Predicate<AnyType> predicate) {
    if (predicate == null)
      throw new NullPointerException("Predicate is null");
    Node<AnyType> curr = head;
    while (curr != null) {
      if (predicate.test(curr.data)) {
        Node<AnyType> p = curr.prev;
        Node<AnyType> n = curr.next;
        if (p == null) {
          head = n;
        } else {
          p.next = n;
        }
        if (n == null) {
          tail = p;
        } else {
          n.prev = p;
        }
        size--;
        return true;
      }
      curr = curr.next;
    }
    return false;
  }

  public void clear() {
    head = tail = null;
    size = 0;
  }

  @Override
  public Iterator<AnyType> iterator() {
    return new Iterator<AnyType>() {
      private Node<AnyType> current = head;

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public AnyType next() {
        if (current == null)
          throw new NoSuchElementException("No more elements");
        AnyType data = current.data;
        current = current.next;
        return data;
      }
    };
  }
}
