package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.*;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 7/15/2016.
 */
public class FluentListImpl<T> implements FluentList<T> {

	private final ArrayList<T> source;

	public FluentListImpl() {
		this.source = new ArrayList<>();
	}

	public FluentListImpl(ArrayList<T> source) {
		this.source = source;
	}




	@Override
	public <R> FluentList<R> map(Function<? super T, ? extends R> func) {
		ArrayList<R> result = new ArrayList<>();

		for(T in : source) {
			result.add(func.apply(in));
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public <R> FluentList<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func) {
		ArrayList<R> result = new ArrayList<>();

		for(T in : source) {
			Iterable<? extends R> step = func.apply(in);

			if(step != null)
				for(R r : step)
					result.add(r);
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> filter(Function<? super T, ? extends Boolean> func) {
		ArrayList<T> result = new ArrayList<>();

		for(T in : source) {
			if(func.apply(in))
				result.add(in);
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public <R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;

		for(T in : source) {
			result = func.apply(result, in);
		}

		return result;
	}

	@Override
	public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func) {
		if(isEmpty())
			return Option.none();

		Iterator<T> it = source.iterator();
		T result = it.next();

		while(it.hasNext()) {
			result = func.apply(result, it.next());
		}

		return Option.some(result);
	}

	@Override
	public FluentList<T> peek(Consumer<? super T> func) {
		forEach(func);
		return this;
	}

	@Override
	public boolean anyMatch(Function<? super T, ? extends Boolean> func) {

		for(T in : source) {
			if(func.apply(in))
				return true;
		}

		return false;
	}

	@Override
	public boolean allMatch(Function<? super T, ? extends Boolean> func) {
		for(T in : source) {
			if(!func.apply(in))
				return false;
		}

		return true;
	}

	@Override
	public void forEach(Consumer<? super T> func) {
		source.forEach(func);
	}

	@Override
	public int size() {
		return source.size();
	}

	@Override
	public boolean isEmpty() {
		return source.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return source.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return source.iterator();
	}

	@Override
	public Object[] toArray() {
		return source.toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		return source.toArray(a);
	}

	@Override
	public boolean add(T t) {
		return source.add(t);
	}

	@Override
	public boolean remove(Object o) {
		return source.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return source.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return source.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return source.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return source.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return source.retainAll(c);
	}

	@Override
	public void clear() {
		source.clear();
	}

	@Override
	public T get(int index) {
		return source.get(index);
	}

	@Override
	public T set(int index, T element) {
		return source.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		source.add(index, element);
	}

	@Override
	public T remove(int index) {
		return source.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return source.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return source.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return source.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return source.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return source.subList(fromIndex, toIndex);
	}

	@Override
	public FluentList<T> toList() {
		return this;
	}

	@Override
	public LazyList<T> toLazyList() {
		return LazyList.from(this);
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
		return source.stream();
	}

	@Override
	public FluentList<T> take(int num) {
		ArrayList<T> result = new ArrayList<>();

		Iterator<T> in = source.iterator();
		for(int i = 0; in.hasNext() && i < num; i++)
			result.add(in.next());

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> takeWhile(Function<? super T, ? extends Boolean> func) {
		ArrayList<T> result = new ArrayList<>();

		for (T in : source) {
			if (func.apply(in))
				result.add(in);
			else
				break;
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> drop(int num) {
		return FluentList.from(subList(num, size()));
	}

	@Override
	public FluentList<T> dropWhile(Function<? super T, ? extends Boolean> func) {
		ArrayList<T> result = new ArrayList<>();
		boolean dropping = true;

		for (T in : source) {
			if(dropping) {
				dropping = func.apply(in);
				if(!dropping)
					result.add(in);
			} else {
				result.add(in);
			}
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> slice(int start, int take) {
		return drop(start).take(take);
	}

	@SafeVarargs
	@Override
	public final FluentList<T> concat(T... source) {
		ArrayList<T> result = new ArrayList<>(this.source);
		result.addAll(Arrays.asList(source));
		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> concat(Iterator<T> source) {
		ArrayList<T> result = new ArrayList<>(this.source);

		while(source.hasNext())
			result.add(source.next());

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> concat(Iterable<T> source) {
		ArrayList<T> result = new ArrayList<>(this.source);

		for(T in : source)
			result.add(in);

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> concat(Collection<T> source) {
		ArrayList<T> result = new ArrayList<>(this.source);
		result.addAll(source);
		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> concat(Stream<T> source) {
		ArrayList<T> result = new ArrayList<>(this.source);
		result.addAll(source.collect(Collectors.toList()));
		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<T> concat(Seq<T> source) {
		ArrayList<T> result = new ArrayList<>(this.source);
		result.addAll(source.toList());
		return new FluentListImpl<>(result);
	}

	@Override
	public <U> FluentList<Tuple2<T, U>> zip(Iterator<U> other) {
		ArrayList<Tuple2<T, U>> result = new ArrayList<>();
		Iterator<T> source = this.source.iterator();

		while(source.hasNext() && other.hasNext()) {
			result.add(Tuple.of(source.next(), other.next()));
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public <U, R> FluentList<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func) {
		ArrayList<R> result = new ArrayList<>();
		Iterator<T> source = this.source.iterator();

		while(source.hasNext() && other.hasNext()) {
			result.add(func.apply(source.next(), other.next()));
		}

		return new FluentListImpl<>(result);
	}

	@Override
	public FluentList<Tuple2<T, Integer>> zipWithIndex() {
		ArrayList<Tuple2<T, Integer>> result = new ArrayList<>();
		int i = 0;

		for(T in : source)
			result.add(Tuple.of(in, i++));

		return new FluentListImpl<>(result);
	}


}
