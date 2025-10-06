package Binary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class BinarySearchTreeTest {

  @Test
  void newTree_hasSizeZero_andHeightMinusOne() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    assertEquals(0, bst.size());
    assertEquals(-1, bst.height(), "Empty tree height should be -1");
  }

  @Test
  void add_and_contains_work_and_noDuplicatesIncreaseSize() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.add(5);
    bst.add(2);
    bst.add(8);
    bst.add(2); // duplicate, should not increase size if duplicates ignored

    assertTrue(bst.contains(5));
    assertTrue(bst.contains(2));
    assertTrue(bst.contains(8));
    assertFalse(bst.contains(7));

    assertEquals(3, bst.size(), "Size should not increase on duplicate insert");
    assertTrue(bst.height() >= 1); // structure-dependent; at least 1 with three nodes
  }

  @Test
  void inOrder_iterator_returnsSortedSequence() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    int[] vals = { 5, 3, 7, 2, 4, 6, 8 };
    for (int v : vals)
      bst.add(v);

    List<Integer> seen = new ArrayList<>();
    for (int x : bst)
      seen.add(x); // iterator() uses inOrder

    assertEquals(List.of(2, 3, 4, 5, 6, 7, 8), seen);
  }

  @Test
  void preOrder_and_postOrder_haveExpectedOrder() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    // Build a known shape
    int[] vals = { 5, 3, 7, 2, 4, 6, 8 };
    for (int v : vals)
      bst.add(v);

    List<Integer> pre = new ArrayList<>();
    for (int x : bst.preOrder())
      pre.add(x);
    assertEquals(List.of(5, 3, 2, 4, 7, 6, 8), pre);

    List<Integer> post = new ArrayList<>();
    for (int x : bst.postOrder())
      post.add(x);
    assertEquals(List.of(2, 4, 3, 6, 8, 7, 5), post);
  }

  @Test
  void remove_leafNode_reducesSize_and_notContains() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    int[] vals = { 5, 3, 7 };
    for (int v : vals)
      bst.add(v);

    assertTrue(bst.remove(3));
    assertEquals(2, bst.size());
    assertFalse(bst.contains(3));
  }

  @Test
  void remove_nodeWithOneChild_reducesSize_and_keepsStructureValid() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.add(5);
    bst.add(3);
    bst.add(7);
    bst.add(9);

    assertTrue(bst.remove(7));
    assertEquals(3, bst.size());
    assertFalse(bst.contains(7));
    assertTrue(bst.contains(9));
    // Sorted order still valid
    List<Integer> seen = new ArrayList<>();
    for (int x : bst)
      seen.add(x);
    assertEquals(List.of(3, 5, 9), seen);
  }

  @Test
  void remove_nodeWithTwoChildren_reducesSizeOnce_and_maintainsOrder() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    int[] vals = { 5, 3, 7, 6, 8 };
    for (int v : vals)
      bst.add(v);

    int sizeBefore = bst.size();
    assertTrue(bst.remove(7)); // two children
    assertEquals(sizeBefore - 1, bst.size(), "Size should decrease by 1 (double-decrement bug?)");

    // Sorted order still valid
    List<Integer> seen = new ArrayList<>();
    for (int x : bst)
      seen.add(x);
    assertEquals(List.of(3, 5, 6, 8), seen);
    assertFalse(bst.contains(7));
  }

  @Test
  void remove_nonExisting_returnsFalse_and_sizeUnchanged() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.add(5);
    bst.add(3);
    bst.add(7);
    int size = bst.size();
    assertFalse(bst.remove(100));
    assertEquals(size, bst.size());
  }

  @Test
  void removeKthLargest_removesCorrectElement_and_throwsOnInvalidK() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    for (int i = 1; i <= 10; i++)
      bst.add(i); // 1..10
    assertEquals(10, bst.size());

    // k = 3 -> remove 8
    bst.removeKthLargest(3);
    assertEquals(9, bst.size());
    assertFalse(bst.contains(8));

    // k out of bounds -> IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> bst.removeKthLargest(0));
    assertThrows(IllegalArgumentException.class, () -> bst.removeKthLargest(100));
  }

  @Test
  void contains_null_throwsIllegalArgumentException() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    assertThrows(IllegalArgumentException.class, () -> bst.contains(null));
  }
}
