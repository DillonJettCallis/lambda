package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.MultiMapSetImpl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public interface MultiMapSet<Key, Value> extends MultiMap<Key, Value> {

	static <Key, Value> MultiMapSet<Key, Value> multiMap() {
		return new MultiMapSetImpl<>();
	}

	@Override
	Set<Value> get(Key key);

	@Override
	Collection<Set<Value>> values();

	@Override
	Set<Map.Entry<Key, Set<Value>>> entrySet();

}
