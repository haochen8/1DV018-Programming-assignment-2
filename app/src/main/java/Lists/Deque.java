package Lists;

import java.util.Iterator;

/**
 * A double-ended queue (deque) interface.
 * @param <AnyType> the type of elements in the deque
 */
public interface Deque<AnyType> extends Iterable<AnyType> {
    void addFirst(AnyType x);
    void addLast(AnyType x);
    AnyType removeFirst();
    AnyType removeLast();
    int size();
    boolean isEmpty();
    Iterator<AnyType> iterator();
}
