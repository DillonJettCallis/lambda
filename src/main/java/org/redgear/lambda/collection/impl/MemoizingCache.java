package org.redgear.lambda.collection.impl;

import org.redgear.lambda.Memoizer;
import org.redgear.lambda.collection.StreamList;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by dcallis on 7/27/2015.
 *
 */
public class MemoizingCache<K, V> implements Map<K, V> {

	private final Map<K, Memoizer<V>> inner = new HashMap<>();

	public MemoizingCache(){

	}

	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	@Override
	public boolean containsKey(Object o) {
		return inner.containsKey(o);
	}

	@Override
	public boolean containsValue(Object o) {
		return inner.containsValue(o);
	}

	@Override
	public V get(Object o) throws ClassCastException {
		return inner.get(o).get();
	}

	@Override
	public V put(K k, V v) {
		return inner.put(k, () -> v).get();
	}

	public void put(K k, Memoizer<V> v){
		inner.put(k, v);
	}

	@Override
	public V remove(Object o) {
		return inner.remove(o).get();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		map.forEach(this::put);
	}

	@Override
	public void clear() {
		inner.clear();
	}

	@Override
	public Set<K> keySet() {
		return inner.keySet();
	}

	@Override
	public StreamList<V> values() {
		return StreamList.from(inner.values()).map(Supplier::get);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return StreamList.from(inner.entrySet()).map(e -> (Entry<K, V>) new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue().get())).toSet();
	}
}
