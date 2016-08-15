package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 7/16/2016.
 */
public interface Monadic<T> {


	<R> Monadic<R> map(Function<? super T, ? extends R> func);

	<R> Monadic<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func);

	Monadic<T> filter(Function<? super T, ? extends Boolean> func);

	Monadic<T> peek(Consumer<? super T> func);

	<R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func);

	Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func);


	boolean anyMatch(Function<? super T, ? extends Boolean> func);

	boolean allMatch(Function<? super T, ? extends Boolean> func);

	void forEach(Consumer<? super T> func);

}
