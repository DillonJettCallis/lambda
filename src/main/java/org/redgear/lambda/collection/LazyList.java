package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.redgear.lambda.GenericUtils.none;
import static org.redgear.lambda.GenericUtils.some;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class LazyList<T> extends AbstractList<T> implements Traversable<T> {

	public static final Logger log = LoggerFactory.getLogger(LazyList.class);

	private Iterator<T> source;
	private final ArrayList<T> list;

	private LazyList(Iterator<T> source){
		this.source = source;
		this.list = new ArrayList<>();
	}

	public static <T> LazyList<T> from(Iterator<T> source){
		return new LazyList<>(source);
	}

	public static <T> LazyList<T> from(Stream<T> source){
		return new LazyList<>(source.iterator());
	}

	public static <T> LazyList<T> from(Seq<T> source){
		return new LazyList<>(source.iterator());
	}

	public static <T> LazyList<T> from(Iterable<T> source){
		if(source instanceof LazyList)
			return (LazyList<T>) source;
		else
			return new LazyList<>(source.iterator());
	}

	@SafeVarargs
	public static <T> LazyList<T> from(T... source){
		return new LazyList<>(new ArrayIterator<>(source));
	}

	public LazyList<T> realize() {
		thunk();
		return this;
	}

	public int thunk(){
		return thunk(Integer.MAX_VALUE);
	}

	public int thunk(int amount){
		if(source == null)
			return 0;

		int pulled = 0;
		for(int i = 0; i < amount && source.hasNext(); i++){
			list.add(source.next());
			pulled++;
		}

		if(!source.hasNext()) {
			source = null;
			list.trimToSize();
		}
		return pulled;
	}

	public int lazyIndex(){
		return list.size() - 1;
	}

	public LazyList<T> concat(Iterator<T> other) {
		return from(new JoiningIterator<>(iterator(), other));
	}

	public LazyList<T> concat(Iterable<T> other) {
		return concat(other.iterator());
	}

	public LazyList<T> concat(Stream<T> other) {
		return concat(other.iterator());
	}

	public LazyList<T> concat(T... other) {
		return concat(new ArrayIterator<>(other));
	}

	public LazyList<T> concat(T other) {
		return concat(new SingletonIterator<>(other));
	}

	public LazyList<T> concat(Seq<T> other) {
		return concat(other.iterator());
	}

	public LazyList<T> concat(StreamList<T> other) {
		return concat(other.iterator());
	}

	public LazyList<T> concat(ImmutableList<T> other) {
		return concat(other.iterator());
	}

	@Override
	public Stream<T> toStream() {
		return stream();
	}


	@Override
	public int size() {
		thunk();
		return list.size();
	}

	@Override
	public T head() {
		return headOption().get();
	}

	@Override
	public Option<T> headOption() {
		return isEmpty() ? none() : some(get(0));
	}

	@Override
	public LazyList<T> tail() {
		return isEmpty() ? LazyList.from(EmptyIterator.instance()) : subList(1, Integer.MAX_VALUE);
	}

	@Override
	public LazyList<T> init() {
		return isEmpty() ? LazyList.from(EmptyIterator.instance()) : subList(0, size() - 1);
	}

	@Override
	public T last() {
		return lastOption().get();
	}

	@Override
	public Option<T> lastOption() {
		return isEmpty() ? none() : some(get(size() - 1));
	}

	@Override
	public LazyList<T> take(int num) {
		return from(toSeq().take(num));
	}

	@Override
	public LazyList<T> drop(int num) {
		return from(toSeq().drop(num));
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty() && thunk(1) == 0;
	}

	@Override
	public boolean add(T t) {
		thunk();
		return list.add(t);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return !collection.isEmpty() && super.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return !collection.isEmpty() && super.retainAll(collection);
	}

	@Override
	public void clear() {
		list.clear();
		source = null;
	}

	@Override
	public T get(int i) {
		if(lazyIndex() > i)
			return list.get(i);
		else {
			thunk(i - lazyIndex());
			return list.get(i);
		}
	}

	@Override
	public T set(int i, T t) {
		if(lazyIndex() > i)
			return list.set(i, t);
		else {
			thunk(i - lazyIndex());
			return list.set(i, t);
		}
	}

	@Override
	public void add(int i, T t) {
		if(lazyIndex() > i)
			list.add(i, t);
		else {
			thunk(i - lazyIndex());
			list.add(i, t);
		}
	}

	@Override
	public T remove(int i) {
		if(lazyIndex() > i)
			return list.remove(i);
		else {
			thunk(i - lazyIndex());
			return list.remove(i);
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		return new ListIterator<T>(){

			int index = i;

			@Override
			public boolean hasNext() {
				return LazyList.this.lazyIndex() >= index || LazyList.this.thunk(1) == 1;
			}

			@Override
			public T next() {
				return LazyList.this.get(index++);
			}

			@Override
			public boolean hasPrevious() {
				return index != 0;
			}

			@Override
			public T previous() {
				return LazyList.this.get(--index);
			}

			@Override
			public int nextIndex() {
				return index + 1;
			}

			@Override
			public int previousIndex() {
				return index - 1;
			}

			@Override
			public void remove() {
				LazyList.this.remove(index);
			}

			@Override
			public void set(T t) {
				LazyList.this.set(index, t);
			}

			@Override
			public void add(T t) {
				LazyList.this.add(index, t);
			}
		};
	}

	@Override
	public LazyList<T> subList(int start, int end) {
		return from(Seq.from(this).drop(start).take(end - start));
	}
}
