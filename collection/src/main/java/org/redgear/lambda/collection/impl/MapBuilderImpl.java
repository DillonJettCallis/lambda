package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.MapBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dcallis on 4/5/2016.
 */
public class MapBuilderImpl<K, V> implements MapBuilder<K, V> {

	private final Map<K, V> inner;
	private volatile boolean built = false;

	public MapBuilderImpl(Map<K, V> inner) {
		this.inner = inner;
	}

	public MapBuilderImpl() {
		this(new HashMap<>());
	}


	@Override
	public Map<K, V> build() {
		if(built)
			throw new IllegalStateException("Build can only be called once!");

		built = true;
		return inner;
	}

	@Override
	public MapBuilder<K, V> put(K k, V v) {
		if(built)
			throw new IllegalStateException("Build can only be called once!");

		inner.put(k, v);
		return this;
	}
}
