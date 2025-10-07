package Hashing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

  @Test
  void newTable_isEmpty_andZeroSize() {
    HashTable<String, Integer> ht = new HashTable<>();
    assertTrue(ht.isEmpty());
    assertEquals(0, ht.size());
    assertEquals(0, ht.collisions());
    assertTrue(ht.currentLoadFactor() >= 0.0);
  }

  @Test
  void put_get_contains_overwrite_work() {
    HashTable<String, Integer> ht = new HashTable<>();
    assertNull(ht.put("a", 1));
    assertEquals(1, ht.size());
    assertEquals(1, ht.get("a"));
    assertTrue(ht.containsKey("a"));

    assertEquals(1, ht.put("a", 9)); // return old
    assertEquals(1, ht.size()); // size unchanged on overwrite
    assertEquals(9, ht.get("a"));
  }

  @Test
  void remove_existing_and_missing() {
    HashTable<String, Integer> ht = new HashTable<>();
    ht.put("x", 42);
    assertEquals(42, ht.remove("x"));
    assertEquals(0, ht.size());
    assertNull(ht.get("x"));
    assertFalse(ht.containsKey("x"));

    assertNull(ht.remove("does-not-exist"));
    assertEquals(0, ht.size());
  }

  @Test
  void rehash_preserves_all_entries() {
    HashTable<Integer, Integer> ht = new HashTable<>(4, 0.75);
    int n = 500;
    for (int i = 0; i < n; i++)
      ht.put(i, -i);
    assertEquals(n, ht.size());

    for (int i = 0; i < n; i++) {
      assertTrue(ht.containsKey(i));
      assertEquals(-i, ht.get(i));
    }

    // Rimlig lastfaktor efter rehash (inte ett hårt krav, mest sanity)
    assertTrue(ht.currentLoadFactor() <= 0.90);
  }

  @Test
  void collisions_increase_when_chainingOccurs() {
    HashTable<BadKey, Integer> ht = new HashTable<>(8, 0.9);
    ht.put(new BadKey(1), 1); // chain empty -> no collision
    ht.put(new BadKey(2), 2); // same bucket -> +1 collision
    ht.put(new BadKey(3), 3); // same bucket -> +1 collision
    assertTrue(ht.collisions() >= 2);
  }

  static final class BadKey {
    final int id;

    BadKey(int id) {
      this.id = id;
    }

    @Override
    public int hashCode() {
      return 1;
    } // alla i samma bucket

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof BadKey))
        return false;
      return this.id == ((BadKey) o).id;
    }
  }
}
