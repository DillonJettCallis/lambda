package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.ImmutableList;
import org.redgear.lambda.collection.LazyList;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.collection.StreamList;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.redgear.lambda.GenericUtils.*;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class StreamListImpl<T> implements StreamList<T> {

	private final java.util.List<T> source;

	public StreamListImpl(java.util.List<T> source){
		this.source = source;
	}

	public static <T> StreamList<T> from(java.util.List<T> source){
		if(source instanceof StreamListImpl)
			return (StreamList<T>) source;
		else
			return new StreamListImpl<>(source);
	}

	public static <T> StreamList<T> from(Collection<T> source){
		if(source instanceof java.util.List)
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
	public T head() {
		return headOption().get();
	}

	@Override
	public Option<T> headOption() {
		return isEmpty() ? none() : some(get(0));
	}

	@Override
	public StreamList<T> tail() {
		Iterator<T> next = source.iterator();

		if(next.hasNext())
			next.next();

		return from(next);
	}

	@Override
	public StreamList<T> init() {
		return from(source.subList(0, size() - 1));
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
	public Stream<T> toStream() {
		return source.stream();
	}

	@Override
	public StreamList<T> toStreamList() {
		return this;
	}

	@Override
	public ImmutableList<T> toImmutableList() {
		return ImmutableList.from(source);
	}

	@Override
	public StreamList<T> realize() {
		if(source instanceof LazyList)
			((LazyList) source).thunk();
		return this;
	}

	@Override
	public <Other> Seq<Tuple2<T, Other>> zipWith(Stream<Other> otherSteam) {
		return null;
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
