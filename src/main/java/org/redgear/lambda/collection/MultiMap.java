package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.MultiMapListImpl;
import org.redgear.lambda.collection.impl.MultiMapSetImpl;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public interface MultiMap<Key, Value> {

	static <Key, Value> MultiMapList<Key, Value> multiMapList() {
		return new MultiMapListImpl<>();
	}

	static <Key, Value> MultiMapSet<Key, Value> multiMapSet() {
		return new MultiMapSetImpl<>();
	}

	void put(Key key, Value value);

	boolean containsKey(Key key);

	boolean contains(Key key, Value value);

	boolean containsValue(Value value);

	void putAll(Key key, Iterable<Value> values);

	void remove(Key key);

	void remove(Key key, Value value);

	Set<Key> keySet();

	Collection<Value> get(Key key);

	Collection<? extends Collection<Value>> values();

	Collection<Value> allValues();

	Set<? extends Map.Entry<Key, ? extends Collection<Value>>> entrySet();

	Collection<Tuple2<Key, Value>> pairs();

}
