package org.redgear.lambda.collection;

import org.redgear.lambda.control.Lazy;
import org.redgear.lambda.collection.impl.FluentIterables;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.redgear.lambda.control.Option.none;
import static org.redgear.lambda.control.Option.option;
import static org.redgear.lambda.control.Option.some;

/**
 * Created by dcallis on 11/23/2015.
 */
public interface ImmutableList<Type> extends Traversable<Type> {

	Logger log = LoggerFactory.getLogger(ImmutableList.class);

	default Type head() {
		return headOption().get();
	}

	ImmutableList<Type> tail();

	int size();

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
		return from(FluentIterables.zip(this, other));

//		return foldLeft(Tuple.of(Nil.<Tuple2<Type, Other>>nil(), other), (pair, next) -> {
//					ImmutableList<Other> input = pair.getV2();
//
//					if (input.isEmpty()) //This means the second list is shorter than the first and has of out.
//						return pair;
//
//					Tuple2<Type, Other> nextResult = Tuple.of(next, input.head());
//
//					return Tuple.of(pair.getV1().prepend(nextResult), input.tail());
//				}).getV1();
	}

	default ImmutableList<Tuple2<Type, Integer>> zipWithIndex() {
		return foldLeft(Tuple.of(Nil.<Tuple2<Type, Integer>>nil(), 0), (pair, next) ->
				pair.map1(ls -> ls.prepend(Tuple.of(next, pair.getV2()))).map2(i -> i + 1)).getV1();
	}

	default Stream<Type> toStream() {
		return StreamBuilder.from(iterator()).stream();
	}

	default boolean equals(ImmutableList<?> other) {
		if(isEmpty() && other.isEmpty())
			return true;

		if(Objects.equals(head(), other.head()))
			return tail().equals(other.tail());
		else
			return false;
	}

	class Link<Type> implements ImmutableList<Type> {

		private final Type head;
		private final ImmutableList<Type> tail;
		private final int size;
		private final Lazy<Integer> hashcode = Lazy.of(() -> toList().hashCode());

		Link(Type head, ImmutableList<Type> tail) {
			this.head = head;
			this.tail = tail;
			this.size = tail.size() + 1;
		}

		@Override
		public Option<Type> headOption() {
			return some(head);
		}

		@Override
		public ImmutableList<Type> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty(){
			return false;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ImmutableList && equals((ImmutableList) other);
		}

		@Override
		public int hashCode() {
			return hashcode.get();
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
			return other instanceof ImmutableList && equals((ImmutableList) other);
		}

		@Override
		public int hashCode() {
			return 0;
		}
	}

	class Join<Type> implements ImmutableList<Type> {

		private final ImmutableList<Type> top;
		private final ImmutableList<Type> bottom;
		private final int size;
		private final Lazy<Integer> hashcode = Lazy.of(() -> toList().hashCode());

		Join(ImmutableList<Type> top, ImmutableList<Type> bottom) {
			this.top = top;
			this.bottom = bottom;
			this.size = top.size() + bottom.size();
		}

		@Override
		public Option<Type> headOption() {
			return top.headOption();
		}

		@Override
		public ImmutableList<Type> tail() {
			ImmutableList<Type> init = top.tail();
			if(init.isEmpty())
				return bottom;
			else
				return new Join<>(init, bottom);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public Iterator<Type> iterator() {
			return new JoiningIterator<>(top.iterator(), bottom.iterator());
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ImmutableList && equals((ImmutableList) other);
		}

		@Override
		public int hashCode() {
			return hashcode.get();
		}
	}

	class IteratorList<Type> implements ImmutableList<Type> {

		private final Option<Type> head;
		private final ImmutableList<Type> tail;
		private final int size;
		private final Lazy<Integer> hashcode = Lazy.of(() -> toList().hashCode());

		IteratorList(Iterator<Type> source) {
			if(source.hasNext()) {
				head = some(source.next());
				tail = new IteratorList<>(source);
				size = tail.size() + 1;
			} else {
				head = none();
				tail = nil();
				size = 0;
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

		@Override
		public int size() {
			return size;
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ImmutableList && equals((ImmutableList) other);
		}

		@Override
		public int hashCode() {
			return hashcode.get();
		}
	}

	class ListList<Type> implements ImmutableList<Type> {

		private final List<Type> array;
		private final Lazy<Integer> hashcode = Lazy.of(() -> toList().hashCode());

		ListList(List<Type> array) {
			this.array = array;
		}

		@Override
		public Option<Type> headOption() {
			return array.isEmpty() ? none() : some(array.get(0));
		}

		@Override
		public ImmutableList<Type> tail() {
			if(array.isEmpty()) {
				return Nil.<Type>nil().tail();
			} else {
				return new ListList<>(array.subList(1, array.size()));
			}
		}

		@Override
		public boolean isEmpty() {
			return array.isEmpty();
		}

		@Override
		public int size() {
			return array.size();
		}

		@Override
		public List<Type> toList() {
			return array;
		}

		@Override
		public Iterator<Type> iterator() {
			return array.iterator();
		}

		@Override
		public String toString() {
			return mkString(", ", "[", "]");
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ImmutableList && equals((ImmutableList) other);
		}

		@Override
		public int hashCode() {
			return hashcode.get();
		}
	}

	static <T> ImmutableList<T> from(Iterable<T> source) {
		if(source instanceof ImmutableList)
			return (ImmutableList<T>) source;
		else
			return from(source.iterator());
	}

	@SafeVarargs
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
