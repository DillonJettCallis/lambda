package org.redgear.lambda;

import org.redgear.lambda.collection.*;
import org.redgear.lambda.concurent.Agent;
import org.redgear.lambda.concurent.Future;
import org.redgear.lambda.concurent.Promise;
import org.redgear.lambda.control.Either;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.control.Try;
import org.redgear.lambda.function.*;
import org.redgear.lambda.tuple.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by dcallis on 4/5/2016.
 */
public class GenericUtils {

	public static <T> FluentIterator<T> iterator() {
		return EmptyIterator.instance();
	}

	public static <T> FluentIterator<T> iterator(T args) {
		return args == null ? iterator() : new SingletonIterator<>(args);
	}

	@SafeVarargs
	public static <T> FluentIterator<T> iterator(T... args) {
		return args == null ? iterator() : new ArrayIterator<>(args);
	}

	public static <T> Iterator<T> iterator(Iterator<T> source) {
		return source == null ? iterator() : source;
	}

	public static <T> Iterator<T> iterator(Iterable<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <T> Iterator<T> iterator(Collection<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <T> Iterator<T> iterator(Stream<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <T> Iterator<T> iterator(Seq<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <T> Iterator<T> iterator(StreamList<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <K, V> Iterator<Tuple2<K, V>> iterator(Map<K, V> source) {
		return stream(source).iterator();
	}

	public static <T> List<T> list() {
		return new ArrayList<>();
	}

	public static <T> List<T> list(T args) {
		List<T> result = list();

		if (args != null)
			result.add(args);

		return result;
	}

	@SafeVarargs
	public static <T> List<T> list(T... args) {
		return args == null ? list() : Arrays.asList(args);
	}

	public static <T> List<T> list(Iterator<T> source) {
		List<T> result = list();

		if(source != null)
			while(source.hasNext())
				result.add(source.next());

		return result;
	}

	public static <T> List<T> list(Iterable<T> source) {
		if(source instanceof Collection)
			return list((Collection<T>) source);
		else if (source instanceof Traversable)
			return ((Traversable<T>) source).toList();
		else
			return list(iterator(source));
	}

	public static <T> List<T> list(Collection<T> source) {
		return source == null ? list() : new ArrayList<>(source);
	}

	public static <T> List<T> list(Stream<T> source) {
		if(source instanceof Seq)
			return ((Seq<T>) source).toList();

		if (source == null)
			return list();
		else
			return source.collect(Collectors.toList());
	}

	public static <T> List<T> list(Seq<T> source) {
		return source == null ? list() : source.toList();
	}

	public static <T> List<T> list(StreamList<T> source) {
		return source == null ? list() : source.toList();
	}

	public static <K, V> List<Tuple2<K, V>> list(Map<K, V> source) {
		return source == null ? list() : stream(source).collect(Collectors.toList());
	}

	public static <T> Stream<T> stream() {
		return Stream.empty();
	}

	public static <T> Stream<T> stream(T args) {
		return args == null ? Stream.empty() : Stream.of(args);
	}

	@SafeVarargs
	public static <T> Stream<T> stream(T... args) {
		return args == null ? Stream.empty() : Stream.of(args);
	}

	public static <T> Stream<T> stream(Iterator<T> source){
		return source == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliterator(source, Long.MAX_VALUE, 0), false);
	}

	public static <T> Stream<T> stream(Iterable<T> source){
		if(source instanceof Traversable)
			return ((Traversable<T>) source).toStream();
		else if(source instanceof Collection)
			return ((Collection<T>) source).stream();
		else
			return source == null ? Stream.empty() : StreamSupport.stream(source.spliterator(), false);
	}

	public static <T> Stream<T> stream(Collection<T> source) {
		return source == null ? stream() : source.stream();
	}

	public static <T> Stream<T> stream(Stream<T> source) {
		return source == null ? stream() : source;
	}

	public static <T> Stream<T> stream(Seq<T> source) {
		return source == null ? stream() : source.toStream();
	}

	public static <T> Stream<T> stream(StreamList<T> source) {
		return source == null ? stream() : source.toStream();
	}

	public static <K, V> Stream<Tuple2<K, V>> stream(Map<K, V> source) {
		return source.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()));
	}

	public static <T> Set<T> set() {
		return new HashSet<>();
	}

	public static <T> Set<T> set(T args) {
		Set<T> result = set();

		if(args != null)
			result.add(args);

		return result;
	}

	@SafeVarargs
	public static <T> Set<T> set(T... args) {
		Set<T> result = set();

		if(args != null)
			Collections.addAll(result, args);

		return result;
	}

	public static <T> Set<T> set(Iterator<T> source) {
		Set<T> result = set();

		if(source != null)
			while(source.hasNext())
				result.add(source.next());

		return result;
	}

	public static <T> Set<T> set(Iterable<T> source) {
		if(source == null)
			return set();
		if(source instanceof Traversable)
			return ((Traversable<T>) source).toSet();
		else if (source instanceof Collection) {
			Set<T> result = set();
			result.addAll(((Collection<T>) source));
			return result;
		} else {
			Set<T> result = set();
			for(T t : source)
				result.add(t);
			return result;
		}
	}

	public static <T> Set<T> set(Collection<T> source) {
		Set<T> set = set();

		set.addAll(source);

		return set;
	}

	public static <T> Set<T> set(Stream<T> source) {
		if(source == null)
			return set();

		if(source instanceof Traversable)
			return ((Traversable<T>) source).toSet();

		return source.collect(Collectors.toSet());
	}

	public static <T> Set<T> set(Seq<T> source) {
		return source == null ? set() : source.toSet();
	}

	public static <T> Set<T> set(StreamList<T> source) {
		return source == null ? set() : source.toSet();
	}

	public static  <K, V> Set<Tuple2<K, V>> set(Map<K, V> source)  {
		return stream(source).collect(Collectors.toSet());
	}

	public static <T> Seq<T> seq() {
		return Seq.from(stream());
	}

	public static <T> Seq<T> seq(T source) {
		return seq(Stream.of(source));
	}

	@SafeVarargs
	public static <T> Seq<T> seq(T... source) {
		return seq(Stream.of(source));
	}

	public static <T> Seq<T> seq(Iterator<T> source) {
		return seq(stream(source));
	}

	public static <T> Seq<T> seq(Iterable<T> source) {
		return seq(stream(source));
	}

	public static <T> Seq<T> seq(Collection<T> source) {
		return seq(stream(source));
	}

	public static <T> Seq<T> seq(Stream<T> source) {
		return Seq.from(source == null ? Stream.empty() : source);
	}

	public static <T> Seq<T> seq(Seq<T> source) {
		return source == null ? seq() : source;
	}

	public static <T> Seq<T> seq(StreamList<T> source) {
		return source == null ? seq() : source;
	}

	public static <K, V> Seq<Tuple2<K, V>> seq(Map<K, V> source) {
		return Seq.from(stream(source));
	}

	public static <T> ImmutableList<T> immutableList() {
		return ImmutableList.nil();
	}

	public static <T> ImmutableList<T> immutableList(T args) {
		return ImmutableList.from(args);
	}

	@SafeVarargs
	public static <T> ImmutableList<T> immutableList(T... args) {
		return ImmutableList.from(args);
	}

	public static <T> ImmutableList<T> immutableList(Iterator<T> source) {
		return ImmutableList.from(source);
	}

	public static <T> ImmutableList<T> immutableList(Iterable<T> source) {
		return ImmutableList.from(source);
	}

	public static <T> ImmutableList<T> immutableList(Stream<T> source) {
		return ImmutableList.from(source);
	}

	public static <T> ImmutableList<T> immutableList(Seq<T> source) {
		return ImmutableList.from(stream(source));
	}

	public static <T> ImmutableList<T> immutableList(StreamList<T> source) {
		return ImmutableList.from(stream(source));
	}

	public static <K, V> ImmutableList<Tuple2<K, V>> immutableList(Map<K, V> source) {
		return ImmutableList.from(stream(source));
	}

	public static <T> StreamList<T> streamList() {
		return StreamList.from(list());
	}

	public static <T> StreamList<T> streamList(T args) {
		return StreamList.from(list(args));
	}

	@SafeVarargs
	public static <T> StreamList<T> streamList(T... args) {
		return StreamList.from(list(args));
	}

	public static <T> StreamList<T> streamList(Iterator<T> source) {
		return StreamList.from(source);
	}

	public static <T> StreamList<T> streamList(Iterable<T> source) {
		return StreamList.from(source);
	}

	public static <T> StreamList<T> streamList(Collection<T> source) {
		return StreamList.from(source);
	}

	public static <T> StreamList<T> streamList(Stream<T> source) {
		return StreamList.from(source);
	}

	public static <T> StreamList<T> streamList(Seq<T> source) {
		return StreamList.from(stream(source));
	}

	public static <T> StreamList<T> streamList(StreamList<T> source) {
		return source == null ? streamList() : source;
	}

	public static <K, V> StreamList<Tuple2<K, V>> streamList(Map<K, V> source) {
		return StreamList.from(stream(source));
	}

	public static <K, V> MapBuilder<K, V> mapBuilder() {
		return MapBuilder.builder();
	}

	public static <K, V> Map<K, V> map() {
		return new HashMap<>();
	}

	public static <K, V> Map<K, V> map(List<K> k1, List<V> v1) {
		Map<K, V> map = new HashMap<>();

		for(int i = 0; i < k1.size() && i < v1.size(); i++) {
			map.put(k1.get(i), v1.get(i));
		}

		return map;
	}

	public static <K, V> Map<K, V> map(Iterable<Tuple2<K, V>> source) {
		MapBuilder<K, V> builder = mapBuilder();

		source.forEach(builder::put);

		return builder.build();
	}

	public static <K, V> Map<K, V> map(K k1, V v1) {
		Map<K, V> map = new HashMap<>();

		map.put(k1, v1);

		return map;
	}

	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
		return MapBuilder.<K, V>builder().put(k1, v1).put(k2, v2).build();
	}

	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
		return MapBuilder.<K, V>builder().put(k1, v1).put(k2, v2).put(k3, v3).build();
	}

	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return MapBuilder.<K, V>builder().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).build();
	}

	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return MapBuilder.<K, V>builder().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).build();
	}

	public static <T1> Tuple1<T1> tuple(T1 v1) {
		return new Tuple1<>(v1);
	}

	public static <T1, T2> Tuple2<T1, T2> tuple(T1 v1, T2 v2) {
		return new Tuple2<>(v1, v2);
	}

	public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple(T1 v1, T2 v2, T3 v3) {
		return new Tuple3<>(v1, v2, v3);
	}

	public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> tuple(T1 v1, T2 v2, T3 v3, T4 v4) {
		return new Tuple4<>(v1, v2, v3, v4);
	}

	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
		return new Tuple5<>(v1, v2, v3, v4, v5);
	}

	public static <Out> Func0<Out> func(Func0<Out> func) {
		return func;
	}

	public static <Out> Func0<Out> func0(Func0<Out> func) {
		return func;
	}

	public static <In1, Out> Func1<In1, Out> func(Func1<In1, Out> func) {
		return func;
	}

	public static <In1, Out> Func1<In1, Out> func1(Func1<In1, Out> func) {
		return func;
	}

	public static <In1, In2, Out> Func2<In1, In2, Out> func(Func2<In1, In2, Out> func) {
		return func;
	}

	public static <In1, In2, Out> Func2<In1, In2, Out> func2(Func2<In1, In2, Out> func) {
		return func;
	}

	public static <In1, In2, In3, Out> Func3<In1, In2, In3, Out> func(Func3<In1, In2, In3, Out> func) {
		return func;
	}

	public static <In1, In2, In3, Out> Func3<In1, In2, In3, Out> func3(Func3<In1, In2, In3, Out> func) {
		return func;
	}


	public static <In1, In2, In3, In4, Out> Func4<In1, In2, In3, In4, Out> func(Func4<In1, In2, In3, In4, Out> func) {
		return func;
	}

	public static <In1, In2, In3, In4, Out> Func4<In1, In2, In3, In4, Out> func4(Func4<In1, In2, In3, In4, Out> func) {
		return func;
	}

	public static <In1, In2, In3, In4, In5, Out> Func5<In1, In2, In3, In4, In5, Out> func(Func5<In1, In2, In3, In4, In5, Out> func) {
		return func;
	}

	public static <In1, In2, In3, In4, In5, Out> Func5<In1, In2, In3, In4, In5, Out> func5(Func5<In1, In2, In3, In4, In5, Out> func) {
		return func;
	}

	public static <T> Option<T> option(Optional<T> source) {
		return source.map(GenericUtils::option).orElse(none());
	}

	public static <T> Option<T> option(T source) {
		return Option.option(source);
	}

	public static <T> Option<T> some(T source) {
		return Option.some(source);
	}

	public static <T> Option<T> none() {
		return Option.none();
	}

	public static <T> Future<T> future(Func0<T> source) {
		return Future.from(source);
	}

	public static <T> Future<T> future(Func0<T> source, ExecutorService ex) {
		return Future.from(source, ex);
	}

	public static <T> Future<T> future(CompletableFuture<T> source) {
		return Future.from(source);
	}

	public static <T> Future<T> future(java.util.concurrent.Future<T> source) {
		return Future.from(source);
	}

	public static <T> Promise<T> promise() {
		return Promise.promise();
	}

	public static <T> Agent<T> agent(T init) {
		return Agent.from(init);
	}

	public static <T> Agent<T> agent(T init, ExecutorService ex) {
		return Agent.from(init, ex);
	}

	public static <T> Try<T> tri(Func0<T> source) {
		return Try.of(source);
	}

	public static <L, R> Either<L, R> left(L source) {
		return new Either.Left<>(source);
	}

	public static <L, R> Either<L, R> right(R source) {
		return new Either.Right<>(source);
	}

	public static boolean isEmpty(String in) {
		return in == null || in.length() == 0;
	}

	public static boolean isNotEmpty(String in) {
		return !isEmpty(in);
	}

	public static boolean isBlank(String in) {
		if(isEmpty(in))
			return true;

		for(char c : in.toCharArray()) {
			if(!Character.isWhitespace(c))
				return false;
		}

		return true;
	}

	public static boolean isNotBlank(String in) {
		return !isBlank(in);
	}

	public static boolean isEmpty(Collection<?> in) {
		return in == null || in.size() == 0;
	}

	public static boolean isNotEmpty(Collection<?> in) {
		return !isEmpty(in);
	}

	public static String orEmpty(String in) {
		return in == null ? "" : in;
	}

	public static String trimToEmpty(String in) {
		return orEmpty(in).trim();
	}

	public static String trimToNull(String in) {
		String trimmed = trimToEmpty(in);

		return isEmpty(trimmed) ? null : trimmed;
	}

	public static boolean isTrue(Boolean bool) {
		return bool != null && bool;
	}

	public static boolean isFalse(Boolean bool) {
		return bool != null && !bool;
	}

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
			return null;

		return func.apply(t);
	}

	public static <T> Lazy<T> lazy(Supplier<T> source) {
		return Lazy.of(source);
	}

	public static <T> ToString toString(Supplier<T> source) {
		return ToString.from(source);
	}
}
