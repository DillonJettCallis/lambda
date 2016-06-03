package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.redgear.lambda.GenericUtils.*;

/**
 * Created by dcallis on 11/23/2015.
 */
public interface ImmutableList<Type> extends Traversable<Type> {

	default Type head() {
		return headOption().get();
	}

	ImmutableList<Type> tail();

	@Override
	default Type last() {
		return lastOption().get();
	}

	@Override
	default Option<Type> lastOption() {
		return foldLeft(none(), (junk, next) -> option(next));
	}

	default ImmutableList<Type> init() {
		return reverse().tail().reverse();
	}

	default ImmutableList<Type> take(int num) {
		return from(toSeq().take(num));
	}

	default ImmutableList<Type> drop(int num) {
		if(num <= 0 || isEmpty())
			return this;
		else
			return tail().drop(num - 1);
	}

	default ImmutableList<Type> slice(int lower, int upper) {
		return drop(lower).take(upper - lower);
	}

	default ImmutableList<Type> prepend(Type elem) {
		return new Link<>(elem, this);
	}

	default ImmutableList<Type> concat(ImmutableList<Type> other) {
		if(this.isEmpty())
			return other;
		else if(other.isEmpty())
			return this;
		else
			return new Join<>(this, other);

//		return foldRight(other, (next, ls) -> ls.prepend(next));
	}

	default ImmutableList<Type> concat(Iterator<Type> other) {
		return concat(from(other));
	}

	default ImmutableList<Type> concat(Iterable<Type> other) {
		return concat(from(other));
	}

	default ImmutableList<Type> concat(Stream<Type> other) {
		return concat(from(other));
	}

	default ImmutableList<Type> concat(Type... other) {
		return concat(from(other));
	}

	default ImmutableList<Type> concat(Type other) {
		return concat(from(other));
	}

	default ImmutableList<Type> concat(Seq<Type> other) {
		return concat(from(other.toStream()));
	}

	default ImmutableList<Type> concat(StreamList<Type> other) {
		return concat(from(other.iterator()));
	}

	default <Result> Result foldLeft(Result in, BiFunction<? super Result, ? super Type, ? extends Result> func) {
		if(isEmpty())
			return in;
		else
			return tail().foldLeft(func.apply(in, head()), func);
	}

	default <Result> Result foldRight(Result in, BiFunction<? super Type, ? super Result, ? extends Result> func) {
		return reverse().foldLeft(in, (r, l) -> func.apply(l, r));
	}

	default ImmutableList<Type> reverse() {
		return foldLeft(Nil.<Type>nil(), ImmutableList::prepend);
	}

	default Type fold(Type in, BiFunction<? super Type, ? super Type, ? extends Type> func) {
		return foldLeft(in, func);
	}

	default Type reduce(BiFunction<? super Type, ? super Type, ? extends Type> func) {
		return tail().fold(head(), func);
	}

	default <Result> ImmutableList<Result> map(Function<? super Type, ? extends Result> func) {
		return foldLeft(Nil.<Result>nil(), (ls, next) -> ls.prepend(func.apply(next))).reverse();
	}

	default <Result> ImmutableList<Result> flatMap(Function<? super Type, ? extends Iterable<Result>> func) {
		return foldLeft(Nil.<Result>nil(), (ls, next) -> ls.concat(func.apply(next))).reverse();
	}

	default ImmutableList<Type> filter(Function<? super Type, ? extends Boolean> func) {
		return foldLeft(Nil.<Type>nil(), (ls, next) -> func.apply(next) ? ls.prepend(next) : ls).reverse();
	}

	@Override
	default void forEach(Consumer<? super Type> func) {
		foldLeft(null, (nil, next) -> {
			func.accept(next);
			return null;
		});
	}

	default boolean forAny(Function<? super Type, ? extends Boolean> func) {
		return foldLeft(false, (test, next) -> test || func.apply(next));
	}

	default boolean forAll(Function<? super Type, ? extends Boolean> func) {
		return foldLeft(true, (test, next) -> test && func.apply(next));
	}

	default <Other> ImmutableList<Tuple2<Type, Other>> zip(ImmutableList<Other> other) {
		return foldLeft(Tuple.of(Nil.<Tuple2<Type, Other>>nil(), other), (pair, next) -> {
					ImmutableList<Other> input = pair.getV2();

					if (input.isEmpty()) //This means the second list is shorter than the first and has of out.
						return pair;

					Tuple2<Type, Other> nextResult = Tuple.of(next, input.head());

					return Tuple.of(pair.getV1().prepend(nextResult), input.tail());
				}).getV1();
	}

	default ImmutableList<Tuple2<Type, Integer>> zipWithIndex() {
		return foldLeft(Tuple.of(Nil.<Tuple2<Type, Integer>>nil(), 0), (pair, next) ->
				pair.map1(ls -> ls.prepend(Tuple.of(next, pair.getV2()))).map2(i -> i + 1)).getV1();
	}

	default Stream<Type> toStream() {
		return StreamBuilder.from(this).stream();
	}

	class Link<Type> implements ImmutableList<Type> {

		private final Type head;
		private final ImmutableList<Type> tail;

		Link(Type head, ImmutableList<Type> tail) {
			this.head = head;
			this.tail = tail;
		}

		public Option<Type> headOption() {
			return some(head);
		}

		public ImmutableList<Type> tail() {
			return tail;
		}

		public boolean isEmpty(){
			return false;
		}

	}


	class Nil<Type> implements ImmutableList<Type> {

		private static final Nil<?> nil = new Nil<>();

		@SuppressWarnings("unchecked")
		public static <Type> ImmutableList<Type> nil() {
			return (Nil<Type>) nil;
		}

		public Option<Type> headOption() {
			return none();
		}

		public ImmutableList<Type> tail() {
			throw new NoSuchElementException("Attempt to call tail() on Nil");
		}

		public boolean isEmpty(){
			return true;
		}
	}

	class Join<Type> implements ImmutableList<Type> {

		private final ImmutableList<Type> top;
		private final ImmutableList<Type> bottom;

		Join(ImmutableList<Type> top, ImmutableList<Type> bottom) {
			this.top = top;
			this.bottom = bottom;
		}

		@Override
		public Option<Type> headOption() {
			return top.headOption();
		}

		@Override
		public ImmutableList<Type> tail() {
			ImmutableList<Type> init = top.init();
			if(init.isEmpty())
				return bottom;
			else
				return new Join<>(init, bottom);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}
	}

	class IteratorList<Type> implements ImmutableList<Type> {

		private final Option<Type> head;
		private final ImmutableList<Type> tail;

		IteratorList(Iterator<Type> source) {
			if(source.hasNext()) {
				head = Option.some(source.next());
				tail = new IteratorList<>(source);
			} else {
				head = Option.none();
				tail = nil();
			}
		}

		@Override
		public Option<Type> headOption() {
			return head;
		}

		@Override
		public ImmutableList<Type> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty() {
			return !head.isPresent();
		}
	}

	static <T> ImmutableList<T> from(Iterable<T> source) {
		if(source instanceof ImmutableList)
			return (ImmutableList<T>) source;
		else
			return from(source.iterator());
	}

	static <T> ImmutableList<T> from(T... source) {
		return from(new ArrayIterator<>(source));
	}

	static <T> ImmutableList<T> from(Stream<T> source) {
		return from(source.iterator());
	}

	static <T> ImmutableList<T> from(Seq<T> source) {
		return from(source.iterator());
	}

	static <T> ImmutableList<T> from(Iterator<T> source) {
		return new IteratorList<>(source);
	}

	static <T> ImmutableList<T> from(T source) {
		return new Link<>(source, nil());
	}

	static <Type> ImmutableList<Type> nil() {
		return Nil.nil();
	}

}
