package Hashing;

import Lists.MyLinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic hash table backed by separate chaining using {@link MyLinkedList}.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {

  public static final class Entry<K, V> {
    public final K key;
    public V value;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String toString() {
      return "Entry{" + key + "=" + value + "}";
    }
  }

  private MyLinkedList<Entry<K, V>>[] buckets;
  private int capacity;
  private int size;
  private final double loadFactor;
  private long collisions;

  public HashTable() {
    this(16, 0.75);
  }

  public HashTable(int initialCapacity, double loadFactor) {
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException("Initial capacity must be greater than zero");
    }
    if (loadFactor <= 0.0 || loadFactor >= 1.0) {
      throw new IllegalArgumentException("Load factor must be between 0 and 1");
    }
    this.capacity = initialCapacity;
    this.loadFactor = loadFactor;
    this.buckets = newBucketArray(capacity);
    this.size = 0;
    this.collisions = 0;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public long collisions() {
    return collisions;
  }

  public double currentLoadFactor() {
    return capacity == 0 ? 0.0 : (double) size / capacity;
  }

  public void clear() {
    if (buckets == null) {
      return;
    }
    for (int i = 0; i < buckets.length; i++) {
      if (buckets[i] != null) {
        buckets[i].clear();
        buckets[i] = null;
      }
    }
    size = 0;
    collisions = 0;
  }

  public V get(K key) {
    Objects.requireNonNull(key, "key");
    if (capacity == 0) {
      return null;
    }
    int idx = index(key);
    MyLinkedList<Entry<K, V>> chain = buckets[idx];
    if (chain == null || chain.isEmpty()) {
      return null;
    }
    for (Entry<K, V> entry : chain) {
      if (key.equals(entry.key)) {
        return entry.value;
      }
    }
    return null;
  }

  public boolean containsKey(K key) {
    Objects.requireNonNull(key, "key");
    if (capacity == 0) {
      return false;
    }
    int idx = index(key);
    MyLinkedList<Entry<K, V>> chain = buckets[idx];
    if (chain == null || chain.isEmpty()) {
      return false;
    }
    for (Entry<K, V> entry : chain) {
      if (key.equals(entry.key)) {
        return true;
      }
    }
    return false;
  }

  public V put(K key, V value) {
    Objects.requireNonNull(key, "key");
    ensureCapacityForInsert();

    int idx = index(key);
    MyLinkedList<Entry<K, V>> chain = ensureChain(idx);
    for (Entry<K, V> entry : chain) {
      if (key.equals(entry.key)) {
        V oldValue = entry.value;
        entry.value = value;
        return oldValue;
      }
    }

    addEntryToChain(chain, new Entry<>(key, value));
    return null;
  }

  public V remove(K key) {
    Objects.requireNonNull(key, "key");
    if (capacity == 0) {
      return null;
    }
    int idx = index(key);
    MyLinkedList<Entry<K, V>> chain = buckets[idx];
    if (chain == null || chain.isEmpty()) {
      return null;
    }

    V removedValue = null;
    for (Entry<K, V> entry : chain) {
      if (key.equals(entry.key)) {
        removedValue = entry.value;
        break;
      }
    }
    if (removedValue == null) {
      return null;
    }

    boolean removed = chain.removeFirstMatch(entry -> key.equals(entry.key));
    if (removed) {
      size--;
      if (chain.isEmpty()) {
        buckets[idx] = null;
      }
      return removedValue;
    }
    return null;
  }

  public int[] bucketSizes() {
    if (buckets == null) {
      return new int[0];
    }
    int[] sizes = new int[buckets.length];
    for (int i = 0; i < buckets.length; i++) {
      sizes[i] = buckets[i] == null ? 0 : buckets[i].size();
    }
    return sizes;
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return new Iterator<>() {
      private int bucketIdx = 0;
      private Iterator<Entry<K, V>> bucketIt = advanceToNextBucket();

      private Iterator<Entry<K, V>> advanceToNextBucket() {
        while (bucketIdx < capacity) {
          MyLinkedList<Entry<K, V>> chain = buckets[bucketIdx++];
          if (chain != null && !chain.isEmpty()) {
            return chain.iterator();
          }
        }
        return null;
      }

      @Override
      public boolean hasNext() {
        if (bucketIt == null) {
          return false;
        }
        if (bucketIt.hasNext()) {
          return true;
        }
        bucketIt = advanceToNextBucket();
        return bucketIt != null && bucketIt.hasNext();
      }

      @Override
      public Entry<K, V> next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return bucketIt.next();
      }
    };
  }

  private void ensureCapacityForInsert() {
    if (capacity == 0) {
      rehash(1);
    } else if ((double) (size + 1) / capacity > loadFactor) {
      rehash(capacity * 2);
    }
  }

  private int index(Object key) {
    int hash = key.hashCode();
    hash ^= (hash >>> 16);
    hash &= 0x7fffffff;
    return hash % capacity;
  }

  private MyLinkedList<Entry<K, V>> ensureChain(int idx) {
    MyLinkedList<Entry<K, V>> chain = buckets[idx];
    if (chain == null) {
      chain = new MyLinkedList<>();
      buckets[idx] = chain;
    }
    return chain;
  }

  private void addEntryToChain(MyLinkedList<Entry<K, V>> chain, Entry<K, V> entry) {
    if (!chain.isEmpty()) {
      collisions++;
    }
    chain.addLast(entry);
    size++;
  }

  private void rehash(int newCapacity) {
    MyLinkedList<Entry<K, V>>[] oldBuckets = buckets;
    int oldSize = size;

    capacity = Math.max(newCapacity, 1);
    buckets = newBucketArray(capacity);
    size = 0;
    collisions = 0;

    if (oldBuckets == null) {
      return;
    }

    for (MyLinkedList<Entry<K, V>> chain : oldBuckets) {
      if (chain == null || chain.isEmpty()) {
        continue;
      }
      for (Entry<K, V> entry : chain) {
        int idx = index(entry.key);
        MyLinkedList<Entry<K, V>> newChain = ensureChain(idx);
        addEntryToChain(newChain, new Entry<>(entry.key, entry.value));
      }
    }

    assert size == oldSize;
  }

  @SuppressWarnings("unchecked")
  private MyLinkedList<Entry<K, V>>[] newBucketArray(int capacity) {
    return (MyLinkedList<Entry<K, V>>[]) new MyLinkedList[capacity];
  }
}
