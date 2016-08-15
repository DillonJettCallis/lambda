package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.*;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.redgear.lambda.control.Option.none;

/**
 * Created by LordBlackHole on 7/17/2016.
 */
public class LazyListImpl<T> extends AbstractList<T> implements LazyList<T> {

	private FluentIterator<T> source;
	private final ArrayList<T> list;

	public LazyListImpl(Iterator<T> source){
		this.source = FluentIterator.from(source);
		this.list = new ArrayList<>();
	}

	private int thunk(){
		return thunk(Integer.MAX_VALUE);
	}

	private int thunk(int amount){
		if(source == null || amount <= 0)
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

	private int lazyIndex(){
		return list.size() - 1;
	}

	private boolean ensureIndex(int index) {
		thunk(index - lazyIndex());
		return index <= lazyIndex();
	}

	@Override
	public FluentIterator<T> iterator() {
		return FluentIterator.from(super.iterator());
	}

	public LazyList<T> concat(Iterator<T> other) {
		return LazyList.from(JoiningIterator.from(iterator(), other));
	}

	public LazyList<T> concat(Iterable<T> other) {
		return concat(other.iterator());
	}

	@Override
	public LazyList<T> concat(Collection<T> source) {
		return concat(source.iterator());
	}

	public LazyList<T> concat(Stream<T> other) {
		return concat(other.iterator());
	}

	@Override
	public <R> LazyList<R> map(Function<? super T, ? extends R> func) {
		return new LazyListImpl<>(iterator().map(func));
	}

	@Override
	public <R> LazyList<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func) {
		return new LazyListImpl<>(iterator().flatMap(func));
	}

	@Override
	public LazyList<T> filter(Function<? super T, ? extends Boolean> func) {
		return new LazyListImpl<>(iterator().filter(func));
	}

	@Override
	public <R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;
		thunk();


		for(T in : list)
			result = func.apply(result, in);

		return result;
	}

	@Override
	public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func) {
		if(isEmpty())
			return none();
		else
			return iterator().reduce(func);
	}

	@Override
	public LazyList<T> peek(Consumer<? super T> func) {
		return new LazyListImpl<>(iterator().peek(func));
	}

	@Override
	public void forEach(Consumer<? super T> func) {
		thunk();

		list.forEach(func);
	}

	@Override
	public boolean anyMatch(Function<? super T, ? extends Boolean> func) {

		for(T in : this) {
			if(func.apply(in))
				return true;
		}

		return false;
	}

	@Override
	public boolean allMatch(Function<? super T, ? extends Boolean> func) {
		thunk();

		for(T in : this) {
			if(!func.apply(in))
				return false;
		}

		return true;
	}

	@SafeVarargs
	public final LazyList<T> concat(T... other) {
		return concat(ArrayIterator.from(other));
	}

	public LazyList<T> concat(T other) {
		return concat(SingletonIterator.from(other));
	}

	public LazyList<T> concat(Seq<T> other) {
		return concat(other.iterator());
	}

	@Override
	public <U> LazyList<Tuple2<T, U>> zip(Iterator<U> other) {
		return new LazyListImpl<>(iterator().zip(other));
	}

	@Override
	public <U, R> LazyList<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func) {
		return new LazyListImpl<>(iterator().zipWith(other, func));
	}

	@Override
	public LazyList<Tuple2<T, Integer>> zipWithIndex() {
		return new LazyListImpl<>(iterator().zipWithIndex());
	}

	@Override
	public FluentList<T> toList() {
		return FluentList.from(this);
	}

	@Override
	public LazyList<T> toLazyList() {
		return this;
	}

	@Override
	public Chain<T> toChain() {
		return Chain.from(this);
	}

	@Override
	public FluentSet<T> toSet() {
		return FluentSet.from(this);
	}

	@Override
	public Seq<T> toSeq() {
		return Seq.from(this);
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
		ensureIndex(i);
		return list.get(i);
	}

	@Override
	public T set(int i, T t) {
		ensureIndex(i);
		return list.set(i, t);
	}

	@Override
	public void add(int i, T t) {
		ensureIndex(i);
		list.add(i, t);
	}

	@Override
	public T remove(int i) {
		ensureIndex(i - lazyIndex());
		return list.remove(i);
	}

	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		LazyListImpl<T> self = this;

		return new ListIterator<T>(){

			int index = i;

			@Override
			public boolean hasNext() {
				return self.ensureIndex(index);
			}

			@Override
			public T next() {
				return self.get(index++);
			}

			@Override
			public boolean hasPrevious() {
				return index != 0;
			}

			@Override
			public T previous() {
				return self.get(--index);
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
				self.remove(index);
			}

			@Override
			public void set(T t) {
				self.set(index, t);
			}

			@Override
			public void add(T t) {
				self.add(index, t);
			}
		};
	}

	@Override
	public LazyList<T> subList(int start, int end) {
		return LazyList.from(Seq.from(this).drop(start).take(end - start));
	}

}
