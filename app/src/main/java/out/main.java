package out;

public class main {
  public static void main(String[] args) {
    HashTable<String, Integer> ht = new HashTable<>();
    ht.put("a", 1);
    ht.put("b", 2);
    ht.put("a", 3); // uppdaterar
    System.out.println(ht.get("a")); // 3
    System.out.println(ht.size()); // 2
    System.out.println(ht.remove("b")); // 2
    System.out.println(ht.get("b")); // null
    System.out.println(ht.collisions()); // >= 0
    for (HashTable.Entry<String, Integer> e : ht) {
      System.out.println(e);
    }

  }
}
