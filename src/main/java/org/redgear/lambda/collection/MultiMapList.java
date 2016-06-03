package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.MultiMapListImpl;

import java.util.*;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public interface MultiMapList<Key, Value> extends MultiMap<Key, Value> {

	static <Key, Value> MultiMapList<Key, Value> multiMap() {
		return new MultiMapListImpl<>();
	}

	@Override
	List<Value> get(Key key);

	@Override
	Collection<List<Value>> values();

	@Override
	Set<Map.Entry<Key, List<Value>>> entrySet();

}
