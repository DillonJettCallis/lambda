package org.redgear.lambda;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class Initializer {

	public static <T> T init(Supplier<T> source, Consumer<? super T> func){
		return modify(source.get(), func);
	}

	public static <T> T initCopy(Supplier<T> source, Function<? super T, ? extends T> func){
		return modifyCopy(source.get(), func);
	}

	public static <T> T modify(T t, Consumer<? super T> func){
		Objects.requireNonNull(func, "Consumer is null");

		if(t == null)
			return null;

		func.accept(t);
		return t;
	}

	public static <T> T modifyCopy(T t, Function<? super T, ? extends T> func) {
		Objects.requireNonNull(func, "Function is null");

		if(t == null)
			return t;

		return func.apply(t);
	}



}
