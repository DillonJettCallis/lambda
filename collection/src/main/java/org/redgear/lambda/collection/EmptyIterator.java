package org.redgear.lambda.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by dcallis on 4/1/2016.
 */
public class EmptyIterator<T> implements FluentIterator<T> {

	public static final EmptyIterator<?> instance = new EmptyIterator<>();

	private EmptyIterator() {

	}

	public static <T> FluentIterator<T> from() {
		return instance();
	}

	@SuppressWarnings("unchecked")
	public static <T> FluentIterator<T> instance() {
		return (FluentIterator<T>) instance;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public T next() {
		throw new NoSuchElementException("EmptyIterator.next()");
	}
}
