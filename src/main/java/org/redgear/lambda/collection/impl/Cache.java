package org.redgear.lambda.collection.impl;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by dcallis on 7/27/2015.
 *
 */
public class Cache<K, V> extends HashMap<K, V> implements Function<K, V> {

	public Function<? super K, ? extends V> mapper;

	public Cache(Function<? super K, ? extends V> mapper){
		this.mapper = mapper;
	}

	@Override
	public V apply(K k){
		return computeIfAbsent(k, mapper);
	}
}
