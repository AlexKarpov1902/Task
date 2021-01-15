package karpov.example;

/**
 *
 * Класс реализует в памяти кэш с двумя стратегиями вытеснения
 *
 * @param <K>
 * @param <V>
 */
public class UniCache<K, V> implements ICache<K, V> {
    ICache<K, V> cache;

    /**
     *
     * @param typeStrategy стратегия вытеснения - 1 LRU
     *                                            2 LFU
     * @param capacity     размер кэша
     */
    public UniCache(int typeStrategy, int capacity) {
        if (typeStrategy == 1) {
            cache = new LRUCache<>(capacity);
        } else {
            cache = new LfuCache<>(capacity);
        }
    }

    @Override
    public void putValue(K key, V value) {
       cache.putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        return cache.getValue(key);
    }

    @Override
    public void printCache() {
        cache.printCache();
    }

    public static void main(String[] args) {

        ICache<Integer, String> cache = new UniCache<>(2, 5);
        for(int i=1; i<=7; i++) {
            cache.putValue(i, String.format("Стр-%d",  i));
        }
        System.out.println("печать кэша после добавления 7 объектов:");
        cache.printCache();

        cache.putValue(3, "Стр-3");
        System.out.println("печать кэша после добавления стр 3:");
        cache.printCache();

        cache.getValue(4);
        System.out.println("печать кэша после запроса стр-4:");
        cache.printCache();

        for(int i=8; i<=11; i++) {
            cache.putValue(i, String.format("стр-%d",  i));
        }
        System.out.println("печать кэша после добавления 4 новых страниц:");
        cache.printCache();

        for (int i = 0; i < 5; i++) {
            cache.getValue(3);
            cache.getValue(4);
        }
        for(int i=12; i<=21; i++) {
            cache.putValue(i, String.format("стр-%d",  i));
        }
        System.out.println("печать кэша после многократного запроса Стр-3 и Стр-4 и добавления новых 10 страниц:");
        cache.printCache();
    }
}
