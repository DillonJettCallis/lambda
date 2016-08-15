package org.redgear.lambda.collection;

import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by LordBlackHole on 7/15/2016.
 */
public class CollectionUtils {

	public static <T> FluentIterator<T> iterator() {
		return EmptyIterator.instance();
	}

	public static <T> FluentIterator<T> iterator(T args) {
		return args == null ? iterator() : SingletonIterator.from(args);
	}

	@SafeVarargs
	public static <T> FluentIterator<T> iterator(T... args) {
		return args == null ? iterator() : ArrayIterator.from(args);
	}

	public static <T> FluentIterator<T> iterator(Iterator<T> source) {
		return source == null ? iterator() : FluentIterator.from(source);
	}

	public static <T> FluentIterator<T> iterator(Iterable<T> source) {
		return source == null ? iterator() : FluentIterator.from(source.iterator());
	}

	public static <T> FluentIterator<T> iterator(Collection<T> source) {
		return source == null ? iterator() : FluentIterator.from(source.iterator());
	}

	public static <T> FluentIterator<T> iterator(Stream<T> source) {
		return source == null ? iterator() : FluentIterator.from(source.iterator());
	}

	public static <T> FluentIterator<T> iterator(Seq<T> source) {
		return source == null ? iterator() : source.iterator();
	}

	public static <K, V> Iterator<Tuple2<K, V>> iterator(Map<K, V> source) {
		return stream(source).iterator();
	}

	public static <T> FluentList<T> list() {
		return FluentList.from();
	}

	public static <T> FluentList<T> list(T args) {
		return FluentList.from(args);
	}

	@SafeVarargs
	public static <T> FluentList<T> list(T... args) {
		return args == null ? list() : FluentList.from(args);
	}

	public static <T> FluentList<T> list(Iterator<T> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <T> FluentList<T> list(Iterable<T> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <T> FluentList<T> list(Collection<T> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <T> FluentList<T> list(Stream<T> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <T> FluentList<T> list(Seq<T> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <K, V> FluentList<Tuple2<K, V>> list(Map<K, V> source) {
		return source == null ? list() : FluentList.from(source);
	}

	public static <T> Stream<T> stream() {
		return Stream.empty();
	}

	public static <T> Stream<T> stream(T args) {
		return Stream.of(args);
	}

	@SafeVarargs
	public static <T> Stream<T> stream(T... args) {
		return args == null ? Stream.empty() : Stream.of(args);
	}

	public static <T> Stream<T> stream(Iterator<T> source){
		return source == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliterator(source, Long.MAX_VALUE, 0), false);
	}

	public static <T> Stream<T> stream(Iterable<T> source){
		if(source instanceof Collection)
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

	public static <K, V> Stream<Tuple2<K, V>> stream(Map<K, V> source) {
		return source.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()));
	}

	public static <T> FluentSet<T> set() {
		return FluentSet.from();
	}

	public static <T> FluentSet<T> set(T args) {
		return FluentSet.from(args);
	}

	@SafeVarargs
	public static <T> FluentSet<T> set(T... source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static <T> FluentSet<T> set(Iterator<T> source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static <T> FluentSet<T> set(Iterable<T> source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static <T> FluentSet<T> set(Collection<T> source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static <T> FluentSet<T> set(Stream<T> source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static <T> FluentSet<T> set(Seq<T> source) {
		return source == null ? set() : FluentSet.from(source);
	}

	public static  <K, V> FluentSet<Tuple2<K, V>> set(Map<K, V> source)  {
		return seq(source).toSet();
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

	public static <K, V> Seq<Tuple2<K, V>> seq(Map<K, V> source) {
		return Seq.from(stream(source));
	}

	public static <T> Chain<T> chain() {
		return Chain.nil();
	}

	public static <T> Chain<T> chain(T args) {
		return Chain.from(args);
	}

	@SafeVarargs
	public static <T> Chain<T> chain(T... args) {
		return Chain.from(args);
	}

	public static <T> Chain<T> chain(Iterator<T> source) {
		return Chain.from(source);
	}

	public static <T> Chain<T> chain(Iterable<T> source) {
		return Chain.from(source);
	}

	public static <T> Chain<T> chain(Stream<T> source) {
		return Chain.from(source);
	}

	public static <T> Chain<T> chain(Seq<T> source) {
		return Chain.from(stream(source));
	}

	public static <K, V> Chain<Tuple2<K, V>> chain(Map<K, V> source) {
		return Chain.from(stream(source));
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

}
