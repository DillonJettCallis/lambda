package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.MultiMapList;
import org.redgear.lambda.collection.MultiMapSet;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.function.Func2;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public class MultiMapSetImpl<Key, Value> implements MultiMapSet<Key,Value> {

	private final Map<Key, Set<Value>> inner = new HashMap<>();

	private Set<Value> newCol() {
		return new HashSet<>();
	}

	@Override
	public void put(Key key, Value value) {
		Set<Value> col = inner.get(key);

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
		Set<Value> col = inner.get(key);

		return col != null && col.contains(value);
	}

	@Override
	public boolean containsValue(Value value) {
		return Seq.from(inner).flatMapIt(Tuple2::getV2).anyMatch(value::equals);
	}

	@Override
	public void putAll(Key key, Iterable<Value> values) {
		Set<Value> col = inner.get(key);

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
		Set<Value> col = inner.get(key);

		if(col != null) {
			col.remove(value);
		}
	}

	@Override
	public Set<Key> keySet() {
		return inner.keySet();
	}

	@Override
	public Set<Value> get(Key key) {
		return inner.get(key);
	}

	@Override
	public Collection<Set<Value>> values() {
		return inner.values();
	}

	@Override
	public Collection<Value> allValues() {
		return Seq.from(inner.values()).flatMapIt(Function.identity()).toList();
	}

	@Override
	public Set<Map.Entry<Key, Set<Value>>> entrySet() {
		return inner.entrySet();
	}

	@Override
	public Collection<Tuple2<Key, Value>> pairs() {
		return Seq.from(inner).flatMapIt(Func2.lift((key, values) -> Seq.from(values).map(value -> Tuple.of(key, value)))).toList();
	}
}
