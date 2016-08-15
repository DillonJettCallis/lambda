package org.redgear.lambda.collection;

/**
 * Created by dcallis on 11/16/2015.
 */
public class ArrayIterator<T> implements FluentIterator<T> {

	private final T[] source;
	private final int length;
	private int index = 0;


	private ArrayIterator(T[] source){
		this.source = source;
		this.length = source.length;
	}

	@SafeVarargs
	public static <T> ArrayIterator<T> from(T... source) {
		return new ArrayIterator<>(source);
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
