package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.StreamListImpl;
import org.redgear.lambda.function.Func1;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public interface StreamList<T> extends java.util.List<T>, Seq<T> {

	Stream<T> stream();

	@Override
	default boolean isParallel(){
		return stream().isParallel();
	}

	@Override
	default Stream<T> sequential(){
		return stream().sequential();
	}

	@Override
	default Stream<T> parallel(){
		return stream().parallel();
	}

	@Override
	default Stream<T> unordered(){
		return stream().unordered();
	}

	@Override
	default Stream<T> onClose(Runnable runnable){
		return stream().onClose(runnable);
	}

	@Override
	default void close(){
		stream().close();
	}

	@Override
	StreamList<T> filter(Predicate<? super T> predicate);

	@Override
	<R> StreamList<R> map(Function<? super T, ? extends R> function);

	@Override
	<R> StreamList<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function);

	@Override
	<R> StreamList<R> flatMapIt(Function<? super T, ? extends Iterable<R>> function);

	@Override
	<R> StreamList<R> flatMapOp(Function<? super T, ? extends Optional<? extends R>> function);

	@Override
	default IntStream mapToInt(ToIntFunction<? super T> toIntFunction){
		return stream().mapToInt(toIntFunction);
	}

	@Override
	default LongStream mapToLong(ToLongFunction<? super T> toLongFunction){
		return stream().mapToLong(toLongFunction);
	}

	@Override
	default DoubleStream mapToDouble(ToDoubleFunction<? super T> toDoubleFunction){
		return stream().mapToDouble(toDoubleFunction);
	}

	@Override
	default IntStream flatMapToInt(Function<? super T, ? extends IntStream> function){
		return stream().flatMapToInt(function);
	}

	@Override
	default LongStream flatMapToLong(Function<? super T, ? extends LongStream> function){
		return stream().flatMapToLong(function);
	}

	@Override
	default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> function){
		return stream().flatMapToDouble(function);
	}

	default StreamList<T> concat (Stream<T> other){
		return from(other.iterator());
	}

	default StreamList<T> concat (Iterable<T> other){
		return from(other.iterator());
	}

	default StreamList<T> concat (Iterator<T> other){
		return from(new JoiningIterator<>(this.iterator(), other));
	}

	default StreamList<T> concat (T other) {
		return from(new SingletonIterator<>(other));
	}

	default StreamList<T> concat (T... other){
		return from(new ArrayIterator<>(other));
	}

	default StreamList<T> concat(Seq<T> other) {
		return concat(other.iterator());
	}

	default StreamList<T> concat(StreamList<T> other) {
		return concat(other.iterator());
	}

	default StreamList<T> concat(ImmutableList<T> other) {
		return concat(other.iterator());
	}

	StreamList<T> tail();

	StreamList<T> init();

	@Override
	StreamList<T> distinct();

	@Override
	StreamList<T> sorted();

	@Override
	StreamList<T> sorted(Comparator<? super T> comparator);

	@Override
	StreamList<T> peek(Consumer<? super T> consumer);

	@Override
	StreamList<T> limit(long l);

	@Override
	StreamList<T> skip(long l);

	@Override
	StreamList<T> subList(int i, int i1);

	StreamList<T> reverse();

	@Override
	default void forEachOrdered(Consumer<? super T> consumer){
		stream().forEachOrdered(consumer);
	}

	@Override
	default void forEach(Consumer<? super T> consumer){
		parallel().forEach(consumer);
	}

	@Override
	default Object[] toArray() {
		return stream().toArray();
	}

	@Override
	default <A> A[] toArray(IntFunction<A[]> intFunction){
		return stream().toArray(intFunction);
	}

	@Override
	default T reduce(T t, BinaryOperator<T> binaryOperator){
		return parallel().reduce(t, binaryOperator);
	}

	@Override
	default Optional<T> reduce(BinaryOperator<T> binaryOperator){
		return parallel().reduce(binaryOperator);
	}

	@Override
	default <U> U reduce(U u, BiFunction<U, ? super T, U> biFunction, BinaryOperator<U> binaryOperator) {
		return parallel().reduce(u, biFunction, binaryOperator);
	}

	@Override
	default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> biConsumer, BiConsumer<R, R> biConsumer1){
		return stream().collect(supplier, biConsumer, biConsumer1);
	}

	@Override
	default <R, A> R collect(Collector<? super T, A, R> collector){
		return parallel().collect(collector);
	}

	@Override
	default Optional<T> min(Comparator<? super T> comparator){
		return parallel().min(comparator);
	}

	@Override
	default Optional<T> max(Comparator<? super T> comparator){
		return parallel().max(comparator);
	}

	@Override
	default long count(){
		return stream().count();
	}

	@Override
	default boolean anyMatch(Predicate<? super T> predicate){
		return stream().anyMatch(predicate);
	}

	@Override
	default boolean allMatch(Predicate<? super T> predicate){
		return stream().allMatch(predicate);
	}

	@Override
	default boolean noneMatch(Predicate<? super T> predicate){
		return stream().noneMatch(predicate);
	}

	@Override
	default Optional<T> findFirst(){
		return stream().findFirst();
	}

	default Optional<T> findFirst(Predicate<? super T> predicate) {
		return stream().filter(predicate).findFirst();
	}

	@Override
	default Optional<T> findAny(){
		return parallel().findAny();
	}

	default Optional<T> findAny(Predicate<? super T> predicate) {
		return parallel().filter(predicate).findFirst();
	}

	@Override
	default Spliterator<T> spliterator(){
		return java.util.List.super.spliterator();
	}

	@Override
	default Set<T> toSet(){
		return new HashSet<>(this);
	}

	static <T> StreamList<T> from(java.util.List<T> source){
		return StreamListImpl.from(source);
	}

	static <T> StreamList<T> from(Collection<T> source){
		return StreamListImpl.from(source);
	}

	static <T> StreamList<T> from(StreamList<T> source){
		return StreamListImpl.from(source.stream());
	}

	static <T> StreamList<T> from(Iterable<T> source){
		return StreamListImpl.from(source);
	}

	static <T> StreamList<T> from(Iterator<T> source){
		return StreamListImpl.from(source);
	}

	static <T> StreamList<T> from(Stream<T> source){
		return StreamListImpl.from(source);
	}

	static <T> StreamList<T> from(T... source){
		return StreamListImpl.from(source);
	}

}
