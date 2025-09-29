package Lists;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LinkedDequeTest {

    @Test
    void newDeque_isEmpty_andSizeZero() {
        Deque<Integer> dq = new LinkedDeque<>();
        assertTrue(dq.isEmpty());
        assertEquals(0, dq.size());
    }

    @Test
    void addFirst_then_removeFirst_preservesLifoAtFront() {
        Deque<String> dq = new LinkedDeque<>();
        dq.addFirst("b");
        dq.addFirst("a"); // [a, b]
        assertEquals(2, dq.size());
        assertFalse(dq.isEmpty());

        assertEquals("a", dq.removeFirst());
        assertEquals("b", dq.removeFirst());
        assertTrue(dq.isEmpty());
        assertEquals(0, dq.size());
    }

    @Test
    void addLast_then_removeLast_preservesLifoAtBack() {
        Deque<Integer> dq = new LinkedDeque<>();
        dq.addLast(1);
        dq.addLast(2);
        dq.addLast(3); // [1,2,3]

        assertEquals(3, dq.size());
        assertEquals(3, dq.removeLast());
        assertEquals(2, dq.removeLast());
        assertEquals(1, dq.removeLast());
        assertTrue(dq.isEmpty());
    }

    @Test
    void mixing_addFirst_addLast_givesExpectedOrder() {
        Deque<Integer> dq = new LinkedDeque<>();
        dq.addFirst(2);  // [2]
        dq.addLast(3);   // [2,3]
        dq.addFirst(1);  // [1,2,3]
        dq.addLast(4);   // [1,2,3,4]

        assertEquals(4, dq.size());
        // Kontrollera via iteration (head -> tail)
        ArrayList<Integer> seen = new ArrayList<>();
        for (int v : dq) seen.add(v);
        assertEquals(java.util.List.of(1,2,3,4), seen);
    }

    @Test
    void remove_onEmpty_throwsIllegalState() {
        Deque<Integer> dq = new LinkedDeque<>();
        assertThrows(IllegalStateException.class, dq::removeFirst);
        assertThrows(IllegalStateException.class, dq::removeLast);
    }

    @Test
    void iterator_traverses_headToTail() {
        Deque<String> dq = new LinkedDeque<>();
        dq.addLast("a");
        dq.addLast("b");
        dq.addLast("c");

        Iterator<String> it = dq.iterator();
        assertTrue(it.hasNext());
        assertEquals("a", it.next());
        assertTrue(it.hasNext());
        assertEquals("b", it.next());
        assertTrue(it.hasNext());
        assertEquals("c", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_next_pastEnd_throwsIllegalState_perCurrentImpl() {
        Deque<Integer> dq = new LinkedDeque<>();
        Iterator<Integer> it = dq.iterator();
        assertThrows(IllegalStateException.class, it::next);
    }

    @Test
    void removeUntilEmpty_updatesHeadTailCorrectly() {
        Deque<Integer> dq = new LinkedDeque<>();
        dq.addFirst(1);      // [1]
        dq.addLast(2);       // [1,2]
        dq.addLast(3);       // [1,2,3]
        assertEquals(3, dq.size());

        assertEquals(1, dq.removeFirst()); // [2,3]
        assertEquals(3, dq.removeLast());  // [2]
        assertEquals(2, dq.removeFirst()); // []
        assertTrue(dq.isEmpty());
        assertEquals(0, dq.size());
        assertThrows(IllegalStateException.class, dq::removeFirst);
        assertThrows(IllegalStateException.class, dq::removeLast);
    }
}
