package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.MultiMap;
import org.redgear.lambda.collection.MultiMapList;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.function.Func2;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public class MultiMapListImpl<Key, Value> implements MultiMapList<Key,Value> {

	private final Map<Key, List<Value>> inner = new HashMap<>();

	private List<Value> newCol() {
		return new ArrayList<>();
	}

	@Override
	public void put(Key key, Value value) {
		List<Value> col = inner.get(key);

		if(col == null) {
			col = newCol();

			inner.put(key, col);
		}

		col.add(value);
	}

	@Override
	public boolean containsKey(Key key) {
		return inner.containsKey(key);
	}

	@Override
	public boolean contains(Key key, Value value) {
		List<Value> col = inner.get(key);

		return col != null && col.contains(value);
	}

	@Override
	public boolean containsValue(Value value) {
		return Seq.from(inner).flatMap(Tuple2::getV2).anyMatch(value::equals);
	}

	@Override
	public void putAll(Key key, Iterable<Value> values) {
		List<Value> col = inner.get(key);

		if(col == null) {
			col = newCol();

			inner.put(key, col);
		}

		for(Value value : values) {
			col.add(value);
		}
	}

	@Override
	public void remove(Key key) {
		inner.remove(key);
	}

	@Override
	public void remove(Key key, Value value) {
		List<Value> col = inner.get(key);

		if(col != null) {
			col.remove(value);
		}
	}

	@Override
	public Set<Key> keySet() {
		return inner.keySet();
	}

	@Override
	public List<Value> get(Key key) {
		return inner.get(key);
	}

	@Override
	public Collection<List<Value>> values() {
		return inner.values();
	}

	@Override
	public Collection<Value> allValues() {
		return Seq.from(inner.values()).flatMap(Function.identity()).toList();
	}

	@Override
	public Set<Map.Entry<Key, List<Value>>> entrySet() {
		return inner.entrySet();
	}

	@Override
	public Collection<Tuple2<Key, Value>> pairs() {
		return Seq.from(inner).flatMap(Func2.lift((key, values) -> Seq.from(values).map(value -> Tuple.of(key, value)))).toList();
	}
}
