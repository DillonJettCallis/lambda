package org.redgear.lambda.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by dcallis on 7/28/2015.
 *
 */
public class JoiningIterator<T> implements FluentIterator<T> {

	private Iterator<? extends T> first;
	private Iterator<? extends T> second;

	public JoiningIterator(Iterator<? extends T> first, Iterator<? extends T> second){
		this.first = first;
		this.second = second;
	}


	@Override
	public boolean hasNext() {
		return first.hasNext() || second.hasNext();
	}

	@Override
	public T next() {
		//Dropping the iterators after they're empty means letting them be GC'd, which could be handy if they're holding onto big collections we don't need.

		if(first.hasNext())
			return first.next();
		else
			first = EmptyIterator.instance();

		if(second.hasNext())
			return second.next();
		else
			second = EmptyIterator.instance();

		throw new NoSuchElementException("No more elements");
	}
}
