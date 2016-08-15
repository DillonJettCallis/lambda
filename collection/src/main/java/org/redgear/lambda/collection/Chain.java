package org.redgear.lambda.collection;

import org.redgear.lambda.control.Lazy;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.control.ToString;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.redgear.lambda.control.Option.*;

/**
 * Created by dcallis on 11/23/2015.
 */
public interface Chain<Type> extends Traversable<Type>, Monadic<Type>, Convertable<Type>, Concatable<Type> {

	default Option<Type> headOption() {
		if(isEmpty())
			return none();
		else
			return some(head());
	}

	Chain<Type> tail();

	@Override
	default Type last() {
		return lastOption().get();
	}

	@Override
	default Option<Type> lastOption() {
		return fold(none(), (junk, next) -> option(next));
	}

	@Override
	default Chain<Type> init() {
		return reverse().tail().reverse();
	}

	default Chain<Type> prepend(Type elem) {
		return new Link<>(elem, this);
	}

	default Chain<Type> concat(Chain<Type> other) {
		return from(iterator().concat(other));
	}

	@Override
	default Chain<Type> concat(Iterator<Type> other) {
		return from(iterator().concat(other));
	}

	@Override
	default Chain<Type> concat(Iterable<Type> other) {
		return from(iterator().concat(other));
	}

	@Override
	default Chain<Type> concat(Stream<Type> other) {
		return from(iterator().concat(other));
	}

	@Override
	default Chain<Type> concat(Type... other) {
		return from(iterator().concat(other));
	}

	@Override
	default Chain<Type> concat(Seq<Type> other) {
		return from(iterator().concat(other));
	}

	default Chain<Type> reverse() {
		return fold(Nil.<Type>nil(), Chain::prepend);
	}

	default <Result> Result fold(Result in, BiFunction<? super Result, ? super Type, ? extends Result> func) {
		return iterator().fold(in, func);
	}

	default Option<Type> reduce(BiFunction<? super Type, ? super Type, ? extends Type> func) {
		return iterator().reduce(func);
	}

	default <Result> Chain<Result> map(Function<? super Type, ? extends Result> func) {
		return from(iterator().map(func));
	}

	default <Result> Chain<Result> flatMap(Function<? super Type, ? extends Iterable<? extends Result>> func) {
		return from(iterator().flatMap(func));
	}

	default Chain<Type> filter(Function<? super Type, ? extends Boolean> func) {
		return from(iterator().filter(func));
	}

	@Override
	default void forEach(Consumer<? super Type> func) {
		iterator().forEach(func);
	}

	default boolean forAny(Function<? super Type, ? extends Boolean> func) {
		return fold(false, (test, next) -> test || func.apply(next));
	}

	default boolean forAll(Function<? super Type, ? extends Boolean> func) {
		return fold(true, (test, next) -> test && func.apply(next));
	}

	@Override
	default <Other> Chain<Tuple2<Type, Other>> zip(Iterator<Other> other) {
		return from(iterator().zip(other));
	}

	@Override
	default <U, R> Chain<R> zipWith(Iterator<U> other, BiFunction<? super Type, ? super U, ? extends R> func) {
		return from(iterator().zipWith(other, func));
	}

	@Override
	default Chain<Tuple2<Type, Integer>> zipWithIndex() {
		return from(iterator().zipWithIndex());
	}

	default Stream<Type> toStream() {
		return StreamBuilder.from(iterator()).stream();
	}

	default String mkString() {
		return toStream().map(String::valueOf).collect(Collectors.joining());
	}

	default String mkString(CharSequence delimiter) {
		return toStream().map(String::valueOf).collect(Collectors.joining(delimiter));
	}

	default String mkString(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return toStream().map(String::valueOf).collect(Collectors.joining(delimiter, prefix, suffix));
	}

	default ToString mkStringLazily() {
		return ToString.from(this::mkString);
	}

	default boolean equals(Chain<?> other) {
		if(isEmpty() && other.isEmpty())
			return true;

		if(Objects.equals(head(), other.head()))
			return tail().equals(other.tail());
		else
			return false;
	}

	@Override
	default Chain<Type> concat(Collection<Type> source) {
		return concat(from(source));
	}

	@Override
	default FluentList<Type> toList() {
		return FluentList.from(this);
	}

	@Override
	default LazyList<Type> toLazyList() {
		return LazyList.from(this);
	}

	@Override
	default Chain<Type> toChain() {
		return this;
	}

	@Override
	default FluentSet<Type> toSet() {
		return FluentSet.from(this);
	}

	@Override
	default Seq<Type> toSeq() {
		return Seq.from(this);
	}

	@Override
	default Chain<Type> peek(Consumer<? super Type> func) {
		forEach(func);
		return this;
	}

	@Override
	default boolean anyMatch(Function<? super Type, ? extends Boolean> func) {

		for(Type in : this) {
			if(func.apply(in))
				return true;
		}

		return false;
	}

	@Override
	default boolean allMatch(Function<? super Type, ? extends Boolean> func) {

		for(Type in : this) {
			if(!func.apply(in))
				return false;
		}

		return true;
	}

	default int size() {
		if(isEmpty())
			return 0;
		else
			return tail().size() + 1;
	}

	class Link<Type> implements Chain<Type> {

		private final Type head;
		private Chain<Type> tail; //Shhhhh! Don't tell anyone tail isn't actually final! from(Iterator) uses this to build a Chain head to tail!

		Link(Type head, Chain<Type> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public Type head() {
			return head;
		}

		@Override
		public Chain<Type> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty(){
			return false;
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Link<?> link = (Link<?>) o;

			if (!head.equals(link.head)) return false;
			return tail.equals(link.tail);

		}

		@Override
		public int hashCode() {
			int result = head.hashCode();
			result = 31 * result + tail.hashCode();
			return result;
		}
	}


	class Nil<Type> implements Chain<Type> {

		private static final Nil<?> nil = new Nil<>();

		@SuppressWarnings("unchecked")
		private static <Type> Chain<Type> nil() {
			return (Nil<Type>) nil;
		}

		public Type head() {
			throw new NoSuchElementException("Attempt to call head() on Nil");
		}

		public Chain<Type> tail() {
			throw new NoSuchElementException("Attempt to call tail() on Nil");
		}

		public boolean isEmpty(){
			return true;
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public boolean equals(Object other) {
			return other == this;
		}

		@Override
		public int hashCode() {
			return 0;
		}
	}

	static <T> Chain<T> from(Iterable<T> source) {
		if(source instanceof Chain)
			return (Chain<T>) source;
		else
			return from(source.iterator());
	}

	@SafeVarargs
	static <T> Chain<T> from(T... source) {
		return from(ArrayIterator.from(source));
	}

	static <T> Chain<T> from(Stream<T> source) {
		return from(source.iterator());
	}

	static <T> Chain<T> from(Seq<T> source) {
		return from(source.iterator());
	}

	static <T> Chain<T> from(Iterator<T> source) {
		if(!source.hasNext())
			return nil();

		Link<T> head = new Link<>(source.next(), null);
		Link<T> working = head;

		while(source.hasNext()) {
			Link<T> temp = new Link<>(source.next(), null);
			working.tail = temp;
			working = temp;
		}

		working.tail = nil();


		return head;
	}

	static <T> Chain<T> from(T source) {
		return new Link<>(source, nil());
	}

	static <Type> Chain<Type> nil() {
		return Nil.nil();
	}

}
