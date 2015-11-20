package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.StreamList;
import org.redgear.lambda.collection.LazyList;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class StreamListImpl<T> implements StreamList<T> {

	private final List<T> source;

	public StreamListImpl(List<T> source){
		this.source = source;
	}

	public static <T> StreamList<T> from(List<T> source){
		if(source instanceof StreamListImpl)
			return (StreamList<T>) source;
		else
			return new StreamListImpl<>(source);
	}

	public static <T> StreamList<T> from(Collection<T> source){
		if(source instanceof List)
			return from((List<T>) source);

		return from(new ArrayList<>(source));
	}

	public static <T> StreamList<T> from(Iterable<T> source){
		if(source instanceof Collection)
			return from((Collection<T>) source);

		return from(LazyList.from(source));
	}

	public static <T> StreamList<T> from(Iterator<T> source){
		return from(LazyList.from(source));
	}

	public static <T> StreamList<T> from(Stream<T> source){
		if(source instanceof StreamListImpl)
			return (StreamList<T>) source;
		else
			return from(LazyList.from(source));
	}

	public static <T> StreamList<T> from(T... source) {
		return from(Arrays.asList(source));
	}

	public static <T> StreamList<T> generate(Supplier<T> source, int size){
		return from(IntStream.range(0, size).mapToObj(i -> source.get()));
	}

	public static <T> StreamList<T> generate(IntFunction<T> source, int size){
		return from(IntStream.range(0, size).mapToObj(source));
	}

	public static <T> StreamList<T> generate(Supplier<Optional<T>> source){
		return from(new Iterator<T>(){

			Optional<T> maybeNext = source.get();

			@Override
			public boolean hasNext() {
				return maybeNext.isPresent();
			}

			@Override
			public T next() {
				T next = maybeNext.get();
				maybeNext = source.get();
				return next;
			}
		});
	}

	public static <T> StreamList<T> generate(T init, Function<? super T, Optional<? extends T>> producer){
		return from(new Iterator<T>(){

			Optional<? extends T> maybeNext = Optional.ofNullable(init);

			@Override
			public boolean hasNext() {
				return maybeNext.isPresent();
			}

			@Override
			public T next() {
				T next = maybeNext.get();
				maybeNext = producer.apply(next);
				return next;
			}
		});
	}

	@Override
	public Stream<T> stream() {
		return source.stream();
	}

	@Override
	public Stream<T> parallel(){
		return source.parallelStream();
	}

	@Override
	public StreamList<T> filter(Predicate<? super T> predicate) {
		return from(stream().filter(predicate));
	}

	@Override
	public <R> StreamList<R> map(Function<? super T, ? extends R> function) {
		return from(stream().map(function));
	}

	@Override
	public <R> StreamList<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function) {
		return from(stream().flatMap(function));
	}

	@Override
	public <R> StreamList<R> flatMapIt(Function<? super T, ? extends Iterable<R>> function) {
		return from(stream().flatMap(t -> from(function.apply(t))));
	}

	@Override
	public <R> StreamList<R> flatMapOp(Function<? super T, ? extends Optional<? extends R>> function) {
		return from(stream().flatMap(t -> function.apply(t).map(Stream::of).orElse(Stream.empty())));
	}

	@Override
	public StreamList<T> ensureSize(){
		if(source instanceof LazyList)
			((LazyList) source).pullAll();
		return this;
	}

	@Override
	public StreamList<T> distinct() {
		return from(stream().distinct());
	}

	@Override
	public StreamList<T> sorted() {
		return from(stream().sorted());
	}

	@Override
	public StreamList<T> sorted(Comparator<? super T> comparator) {
		return from(stream().sorted(comparator));
	}

	@Override
	public StreamList<T> peek(Consumer<? super T> consumer) {
		return from(stream().peek(consumer));
	}

	@Override
	public StreamList<T> limit(long l) {
		return from(stream().limit(l));
	}

	@Override
	public StreamList<T> skip(long l) {
		return from(stream().skip(l));
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
	public <T1> T1[] toArray(T1[] t1s) {
		return source.toArray(t1s);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return source.containsAll(collection);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof StreamListImpl){
			StreamListImpl<?> other = (StreamListImpl) o;
			return source.equals(other.source);
		}
		else
			return false;
	}

	@Override
	public int hashCode() {
		return source.hashCode();
	}

	@Override
	public T get(int i) {
		return source.get(i);
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
	public boolean addAll(Collection<? extends T> collection) {
		return source.addAll(collection);
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> collection) {
		return source.addAll(i, collection);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return source.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return source.retainAll(collection);
	}

	@Override
	public void clear() {
		source.clear();
	}

	@Override
	public T set(int i, T t) {
		return source.set(i, t);
	}

	@Override
	public void add(int i, T t) {
		source.add(i, t);
	}

	@Override
	public T remove(int i) {
		return source.remove(i);
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
	public ListIterator<T> listIterator(int i) {
		return source.listIterator(i);
	}

	@Override
	public List<T> toList(){
		return LazyList.from(stream());
	}

	@Override
	public StreamList<T> subList(int i, int i1) {
		return from(source.subList(i, i1));
	}

	@Override
	public T fold(T start, BiFunction<? super T, ? super T, ? extends T> func) {
		return parallel().reduce(start, func::apply);
	}

	@Override
	public <R> R foldLeft(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;

		for(T element : this)
			result = func.apply(result, element);

		return result;
	}

	@Override
	public <R> R foldRight(R start, BiFunction<? super T, ? super R, ? extends R> func){
		return reverse().foldLeft(start, (r, t) -> func.apply(t, r));
	}

	@Override
	public StreamList<T> reverse() {
		LinkedList<T> values = new LinkedList<>();

		forEach(values::addFirst);

		return from(values);
	}

	@Override
	public String toString(){
		return source.toString();
	}
}
