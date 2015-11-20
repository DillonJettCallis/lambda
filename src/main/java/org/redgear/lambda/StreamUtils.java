package org.redgear.lambda;

import org.redgear.lambda.collection.StreamList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by dcallis on 7/20/2015.
 *
 */
public class StreamUtils {

	public static <T> Stream<T> orEmpty(Stream<T> source){
		return source == null ? Stream.empty() : source;
	}

	public static <T> Stream<T> stream(Collection<T> source){
		return source == null ? Stream.empty() : source.stream();
	}

	public static <T> Optional<T> option(T t) {
		return t == null ? Optional.empty() : Optional.of(t);
	}

	public static <T> T orNull(Optional<T> op){
		return op == null ? null : op.orElse(null);
	}

	public static <In, Out> Stream<Out> flatMap(Stream<In> source, Function<? super In, ? extends Collection<Out>> mapper){
		Objects.requireNonNull(mapper, "Mapper is null");

		return source == null ? Stream.empty() : source.flatMap(in -> stream(mapper.apply(in)));
	}

	public static <T> Stream<T> lazy(Stream<T> sourceStream){
		return StreamList.from(sourceStream); //The cheats!
	}

}
