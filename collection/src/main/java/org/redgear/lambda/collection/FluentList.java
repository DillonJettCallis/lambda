package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.FluentListImpl;
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
public interface FluentList<T> extends List<T>, Monadic<T>, Convertable<T>, Slicable<T>, Concatable<T> {


	<R> FluentList<R> map(Function<? super T, ? extends R> func);

	<R> FluentList<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func);

	FluentList<T> filter(Function<? super T, ? extends Boolean> func);

	<R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func);

	Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func);

	FluentList<T> peek(Consumer<? super T> func);

	void forEach(Consumer<? super T> func);








	FluentList<T> take(int num);

	FluentList<T> takeWhile(Function<? super T, ? extends Boolean> func);

	FluentList<T> drop(int num);

	FluentList<T> dropWhile(Function<? super T, ? extends Boolean> func);

	FluentList<T> slice(int start, int take);





	FluentList<T> concat(T... source);

	FluentList<T> concat(Iterator<T> source);

	FluentList<T> concat(Iterable<T> source);

	FluentList<T> concat(Collection<T> source);

	FluentList<T> concat(Stream<T> source);

	FluentList<T> concat(Seq<T> source);

	<U> FluentList<Tuple2<T, U>> zip(Iterator<U> other);

	<U, R> FluentList<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func);

	FluentList<Tuple2<T, Integer>> zipWithIndex();




	static <T> FluentList<T> from() {
		return new FluentListImpl<>();
	}

	static <T> FluentList<T> from(T source) {
		ArrayList<T> list = new ArrayList<>(1);
		list.add(source);
		return new FluentListImpl<>(list);
	}

	@SafeVarargs
	static <T> FluentList<T> from(T... source) {
		return from(Arrays.asList(source));
	}

	static <T> FluentList<T> from(Iterator<T> source) {
		ArrayList<T> list = new ArrayList<>();
		while(source.hasNext())
			list.add(source.next());
		return new FluentListImpl<>(list);
	}

	static <T> FluentList<T> from(Iterable<T> source) {
		return from(source.iterator());
	}

	static <T> FluentList<T> from(Collection<T> source) {
		return new FluentListImpl<>(new ArrayList<>(source));
	}

	static <T> FluentList<T> from(Stream<T> source) {
		return new FluentListImpl<>(source.collect(Collectors.toCollection(ArrayList::new)));
	}

	static <T> FluentList<T> from(Seq<T> source) {
		return new FluentListImpl<>(source.toStream().collect(Collectors.toCollection(ArrayList::new)));
	}

	static <K, V> FluentList<Tuple2<K, V>> from(Map<K, V> source) {
		return new FluentListImpl<>(source.entrySet().stream().map(Tuple::from).collect(Collectors.toCollection(ArrayList::new)));
	}

}
