package ru.vsu.cs.savchenko_n_a.dataStructure.map;


import ru.vsu.cs.savchenko_n_a.utils.dummy.DefaultNotSupportedMap;
import ru.vsu.cs.savchenko_n_a.utils.dummy.DefaultNotSupportedSet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Класс, реализующий Map в виде хеш-таблицы (достаточно примитивной без
 * перестроения из-за высокой заполненности и т.п.)
 *
 * @param <K>
 * @param <V>
 */
public class SimpleHashMap<K, V> implements DefaultNotSupportedMap<K, V> {

    protected EntryListItem[] table;
    protected int size = 0;

    private int getIndex(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        return index;
    }

    private EntryListItem getEntry(Object key, int index) {
        if (index < 0) {
            index = getIndex(key);
        }
        for (EntryListItem curr = table[index]; curr != null; curr = curr.next) {
            if (key.equals(curr.key)) {
                return curr;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key, -1) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return entrySet().stream().anyMatch(kv -> value.equals(kv.getValue()));
    }

    @Override
    public V get(Object key) {
        EntryListItem item = getEntry(key, -1);
        return (item == null) ? null : item.value;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);
        EntryListItem item = getEntry(key, index);
        if (item != null) {
            V oldValue = item.value;
            item.value = value;
            return oldValue;
        }
        table[index] = new EntryListItem(key, value, table[index]);
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        EntryListItem parent = null;
        for (EntryListItem curr = table[index]; curr != null; curr = curr.next) {
            if (key.equals(curr.key)) {
                if (parent == null) {
                    table[index] = curr.next;
                } else {
                    parent.next = curr.next;
                }
                size--;
                return curr.value;
            }
            parent = curr;
        }
        return null;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    /**
     * Реализация Iterable&lt;Map.Entry&lt;K, V&gt;&gt;
     *
     * @return Итератор
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new DefaultNotSupportedSet<>() {
            @Override
            public int size() {
                return SimpleHashMap.this.size();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<>() {
                    int tableIndex = -1;
                    EntryListItem curr = null;

                    {
                        findNext();
                    }

                    private void findNext() {
                        if (tableIndex >= table.length) {
                            return;
                        }
                        if (curr != null) {
                            curr = curr.next;
                        }
                        if (curr == null) {
                            for (tableIndex = tableIndex + 1; tableIndex < table.length; tableIndex++) {
                                curr = table[tableIndex];
                                if (curr != null) {
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return curr != null;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        Map.Entry<K, V> temp = curr;
                        findNext();
                        return temp;
                    }
                };
            }
        };
    }

    /**
     * Размер/емкость (в корзинах) хеш-таблицы
     */
    public int getCapacity() {
        return table == null ? 0 : table.length;
    }


    // ниже описаны методы, которые считают статистику
    // (исключительно для исследования)

    /**
     * Максимальное кол-во элементов в отдельной ячейке (корзине) хеш-таблицы
     */
    public int getMaxCountInBuckets() {
        int maxCount = 0;
        for (EntryListItem curr : table) {
            int count = 0;
            for (; curr != null; curr = curr.next) {
                count++;
            }
            maxCount = Math.max(maxCount, count);
        }
        return maxCount;
    }

    /**
     * Кол-во не пустых ячеек (корзин) в хеш-таблицы
     */
    public double getLoadedBucketsCount() {
        int count = 0;
        for (EntryListItem curr : table) {
            if (curr != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Кол-во не пустых ячеек (корзин) в хеш-таблицы
     */
    public double getLoadFactor() {
        return getLoadedBucketsCount() / getCapacity();
    }

    /**
     * Средее кол-во элементов в отдельной ячейке (корзине) хеш-таблицы
     */
    public double getAvgCountInBackets() {
        return ((double) size()) / getLoadedBucketsCount();
    }

    private class EntryListItem implements Map.Entry<K, V> {

        public K key;
        public V value;
        public EntryListItem next;

        public EntryListItem(K key, V value, EntryListItem next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
