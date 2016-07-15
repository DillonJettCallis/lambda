package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.MapBuilderImpl;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Collections;
import java.util.Map;

/**
 * Created by dcallis on 4/5/2016.
 *
 * For filling maps with data easier.
 *
 * Build or buildImmutable can only be called once.
 * Attempting a second call to one of them will result in an IllegalStateException.
 */
public interface MapBuilder<K, V> {



	Map<K, V> build();

	default Map<K, V> buildImmutable() {
		return Collections.unmodifiableMap(build());
	}

	MapBuilder<K, V> put(K k, V v);

	default MapBuilder<K, V> put(Tuple2<K, V> pair) {
		return put(pair.v1, pair.v2);
	}

	static <K, V> MapBuilder<K, V> builder() {
		return new MapBuilderImpl<>();
	}

	static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
		return new MapBuilderImpl<>(map);
	}

}
