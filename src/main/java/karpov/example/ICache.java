package karpov.example;


public interface ICache<K, V> {

   void putValue(K key, V value);

   V getValue(K key);

   void printCache();
}
