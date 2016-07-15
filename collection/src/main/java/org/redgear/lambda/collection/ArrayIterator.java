package org.redgear.lambda.collection;

import java.util.Iterator;

/**
 * Created by dcallis on 11/16/2015.
 */
public class ArrayIterator<T> implements FluentIterator<T> {

	private final T[] source;
	private final int length;
	private int index = 0;

	public ArrayIterator(T[] source){
		this.source = source;
		this.length = source.length;
	}

	@Override
	public boolean hasNext() {
		return index < length;
	}

	@Override
	public T next() {
		return source[index++];
	}
}
