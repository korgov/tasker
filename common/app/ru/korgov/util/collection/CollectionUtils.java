package ru.korgov.util.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.korgov.util.Filter;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Fus;
import ru.korgov.util.func.Function;

import java.util.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 12/18/11
 */
public class CollectionUtils {
    public static <K, V> Map<K, V> zipMap(final List<? extends K> keys, final List<? extends V> values) {
        if (keys.size() != values.size()) {
            throw new RuntimeException("List's size must be equals!");
        }
        final int size = keys.size();
        final Map<K, V> out = new HashMap<K, V>();
        for (int i = 0; i < size; ++i) {
            out.put(keys.get(i), values.get(i));
        }
        return out;
    }

    public static <K, V> Map<K, V> zipMap(final Iterable<Pair<? extends K, ? extends V>> entries) {
        return mapFromIterable(Fus.<K, V>firstOfPair(), Fus.<K, V>secondOfPair(), entries);
    }

    public static <K, V> Map<K, V> zipMap(final Pair<? extends K, ? extends V>... entries) {
        return zipMap(list(entries));
    }

    public static <T> List<T> list(final T... items) {
        return Arrays.asList(items);
    }

    public static <T> List<T> emptyIfNull(final List<T> list) {
        return list == null ? Collections.<T>emptyList() : list;
    }

    public static <T> Set<T> emptyIfNull(final Set<T> set) {
        return set == null ? Collections.<T>emptySet() : set;
    }

    public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> map) {
        return map == null ? Collections.<K, V>emptyMap() : map;
    }

    public static <T> void addIfNotNull(final Collection<T> collection, final T item) {
        if (item != null) {
            collection.add(item);
        }
    }

    public static <K, V> void addIfNotNull(final Map<K, V> map, final K key, final V value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    public static <K, V> void appendToMultiMap(final Map<K, List<V>> mm, final K key, final V value) {
        List<V> values = mm.get(key);
        if (values == null) {
            values = Cf.newList();
            mm.put(key, values);
        }
        values.add(value);
    }

    public static <K, V> void appendToMultiSet(final Map<K, Set<V>> m, final K k, final V v) {
        Set<V> values = m.get(k);
        if (values == null) {
            values = Cf.newSet();
            m.put(k, values);
        }
        values.add(v);
    }

    public static <K, V> void appendAllToMultiSet(final Map<K, Set<V>> out, final K key, final Iterable<V> values) {
        for (final V v : values) {
            appendToMultiSet(out, key, v);
        }
    }

    public static <K, V> void appendAllToMultiSet(final Map<K, Set<V>> out, final Map<K, ? extends Iterable<V>> kvss) {
        for (final Map.Entry<K, ? extends Iterable<V>> kvs : kvss.entrySet()) {
            appendAllToMultiSet(out, kvs.getKey(), kvs.getValue());
        }
    }

    @Nullable
    public static <T> T firstOrNull(final Collection<T> c) {
        final Iterator<T> iterator = c.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @NotNull
    public static <T> Option<T> firstOrNothing(final Collection<T> c) {
        final Iterator<T> iterator = c.iterator();
        return iterator.hasNext() ? Option.just(iterator.next()) : Option.<T>nothing();
    }

    public static <T> List<T> filter(final Iterable<? extends T> src, final Filter<? super T> filter) {
        final List<T> out = Cf.newList();
        for (final T t : src) {
            if (filter.fits(t)) {
                out.add(t);
            }
        }
        return out;
    }

    public static <T> Set<T> minus(final Collection<T> l, final Collection<T> r) {
        final Set<T> x = Cf.newSet(r);
        final Set<T> out = Cf.newSet();
        for (final T v : l) {
            if (!x.contains(v)) {
                out.add(v);
            }
        }
        return out;
    }

    public static <T> List<T> join(final Collection<T> l, final Collection<T> r) {
        final List<T> out = Cf.newList(l);
        out.addAll(r);
        return out;
    }

    public static <T> List<T> join(final Collection<? extends Collection<T>> colls) {
        final List<T> out = Cf.newList();
        for (final Collection<T> coll : colls) {
            out.addAll(coll);
        }
        return out;
    }

    public static <F, T> List<T> map(final Iterable<? extends F> src, final Function<F, T> fu) {
        return fu.map(src);
    }

    public static <F, T> List<T> map(final Function<F, T> fu, final Iterable<? extends F> src) {
        return fu.map(src);
    }


    public static <T, K, V> Map<K, V> mapFromIterable(final Function<T, K> k, final Function<T, V> v, final Iterable<T> src) {
        final Map<K, V> out = Cf.newMap();
        for (final T t : src) {
            out.put(k.apply(t), v.apply(t));
        }
        return out;
    }


    public static <T, K, V> Map<K, List<V>> multiMapFromIterable(final Function<T, K> k, final Function<T, V> v, final Iterable<T> src) {
        final Map<K, List<V>> out = Cf.newMap();
        for (final T t : src) {
            appendToMultiMap(out, k.apply(t), v.apply(t));
        }
        return out;
    }

    public static <F, T> List<T> map(final F[] src, final Function<F, T> fu) {
        return map(Cf.list(src), fu);
    }

    public static <T> Iterator<T> oneTimeIterator(final T value) {
        return new Iterator<T>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (hasNext) {
                    hasNext = false;
                    return value;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                hasNext = false;
            }
        };
    }

    public static <T> Iterator<T> emptyIterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
            }
        };
    }

    public static <T> Pair<List<T>, List<T>> split(Iterable<? extends T> ts, Filter<T> f){
        final List<T> fits = Cf.newList();
        final List<T> notFits = Cf.newList();
        for (final T t : ts) {
            if(f.fits(t)){
                fits.add(t);
            } else {
                notFits.add(t);
            }
        }
        return Cf.pair(fits, notFits);
    }

    public static <T> List<List<T>> split(Iterable<? extends T> ts, int chunkSize) {
        final List<List<T>> out = Cf.newList();
        final Iterator<? extends T> iterator = ts.iterator();
        while (iterator.hasNext()) {
            final List<T> chunk = Cf.newList(chunkSize);
            for (int i = 0; i < chunkSize && iterator.hasNext(); ++i) {
                chunk.add(iterator.next());
            }
            out.add(chunk);
        }
        return out;
    }

    public static <T> T maxBy(final Iterable<T> src, final Comparator<T> cmp, final T defaultValue) {
        final Iterator<T> it = src.iterator();
        if (it.hasNext()) {
            T max = it.next();
            while (it.hasNext()) {
                final T some = it.next();
                if (cmp.compare(max, some) < 0) {
                    max = some;
                }
            }
            return max;
        }
        return defaultValue;
    }

    public static <T extends Comparable<T>> T maxBy(final Iterable<T> src, final T defaultValue) {
        return maxBy(src, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        }, defaultValue);
    }

    public static boolean isEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

}
