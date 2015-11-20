package org.redgear.lambda.collection;

import java.util.Iterator;

/**
 * Created by dcallis on 11/16/2015.
 */
public class SingletonInterator<T> implements Iterator<T> {

	private final T item;
	private boolean next = true;

	public SingletonInterator(T item){
		this.item = item;
	}

	@Override
	public boolean hasNext() {
		return next;
	}

	@Override
	public T next() {
		next = false;
		return item;
	}
}
