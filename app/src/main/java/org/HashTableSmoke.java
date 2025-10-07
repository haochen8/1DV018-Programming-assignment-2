package org;

import Hashing.HashTable;

public class HashTableSmoke {
  public static void main(String[] args) {
    HashTable<String, Integer> ht = new HashTable<>(4, 0.75);

    System.out.println("Empty? " + ht.isEmpty()); // true
    System.out.println("put a=1 -> " + ht.put("a", 1)); // null
    System.out.println("put b=2 -> " + ht.put("b", 2)); // null
    System.out.println("get a    = " + ht.get("a")); // 1
    System.out.println("size     = " + ht.size()); // 2
    System.out.println("contains a? " + ht.containsKey("a")); // true
    System.out.println("overwrite a=10 (old=" + ht.put("a", 10) + ")"); // old=1
    System.out.println("get a    = " + ht.get("a")); // 10
    System.out.println("remove b -> " + ht.remove("b")); // 2
    System.out.println("size     = " + ht.size()); // 1
    System.out.println("LF=" + ht.currentLoadFactor() + " collisions=" + ht.collisions());

    // Forcera rehash
    for (int i = 0; i < 100; i++)
      ht.put("k" + i, i);
    System.out.println("After many inserts -> size=" + ht.size() +
        ", LF=" + ht.currentLoadFactor() + ", collisions=" + ht.collisions());
    System.out.println("get k42  = " + ht.get("k42")); // 42
  }
}
