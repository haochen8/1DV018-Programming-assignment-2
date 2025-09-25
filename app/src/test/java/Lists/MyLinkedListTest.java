package Lists;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

  @Test
  void newList_isEmpty_andSizeZero() {
    MyLinkedList<Integer> list = new MyLinkedList<>();
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());
  }

  @Test
  void addFirst_thenRemoveFirst_works() {
    MyLinkedList<String> list = new MyLinkedList<>();
    list.addFirst("b");
    list.addFirst("a");
    assertEquals(2, list.size());
    assertFalse(list.isEmpty());

    assertEquals("a", list.removeFirst());
    assertEquals("b", list.removeFirst());
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());
  }

  @Test
  void addLast_thenRemoveLast_works() {
    MyLinkedList<Integer> list = new MyLinkedList<>();
    list.addLast(1);
    list.addLast(2);
    list.addLast(3);
    assertEquals(3, list.size());

    assertEquals(3, list.removeLast());
    assertEquals(2, list.removeLast());
    assertEquals(1, list.removeLast());
    assertTrue(list.isEmpty());
  }

  @Test
  void mixing_addFirst_addLast_preservesOrder() {
    MyLinkedList<Integer> list = new MyLinkedList<>();
    list.addFirst(2); // [2]
    list.addLast(3); // [2,3]
    list.addFirst(1); // [1,2,3]
    list.addLast(4); // [1,2,3,4]

    assertEquals(4, list.size());
    assertEquals(1, list.get(0));
    assertEquals(2, list.get(1));
    assertEquals(3, list.get(2));
    assertEquals(4, list.get(3));
  }

  @Test
  void get_outOfBounds_throws() {
    MyLinkedList<String> list = new MyLinkedList<>();
    list.addLast("x");
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
  }

  @Test
  void remove_onEmpty_throwsIllegalState() {
    MyLinkedList<Integer> list = new MyLinkedList<>();
    assertThrows(IllegalStateException.class, list::removeFirst);
    assertThrows(IllegalStateException.class, list::removeLast);
  }

  @Test
  void iterator_traverses_inInsertionOrder_headToTail() {
    MyLinkedList<Integer> list = new MyLinkedList<>();
    list.addLast(1);
    list.addLast(2);
    list.addLast(3);

    ArrayList<Integer> seen = new ArrayList<>();
    for (int x : list) {
      seen.add(x);
    }
    assertEquals(java.util.List.of(1, 2, 3), seen);
  }

  @Test
  void iterator_independent_of_get() {
    MyLinkedList<String> list = new MyLinkedList<>();
    list.addFirst("b");
    list.addFirst("a");

    Iterator<String> it = list.iterator();
    assertTrue(it.hasNext());
    assertEquals("a", it.next());
    assertTrue(it.hasNext());
    assertEquals("b", it.next());
  }
}
