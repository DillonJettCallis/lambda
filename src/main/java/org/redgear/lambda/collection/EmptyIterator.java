package org.redgear.lambda.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by dcallis on 4/1/2016.
 */
public class EmptyIterator<T> implements Iterator<T> {

	public static final EmptyIterator<?> instance = new EmptyIterator<>();

	private EmptyIterator() {

	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> instance() {
		return (Iterator<T>) instance;
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
