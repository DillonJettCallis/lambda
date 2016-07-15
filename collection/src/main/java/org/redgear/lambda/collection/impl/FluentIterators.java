package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.*;
import org.redgear.lambda.function.Func;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/9/2016.
 */
public class FluentIterators {

	public static <T> FluentIterator<T> concat(Iterator<T> first, Iterator<T> second) {
		return new JoiningIterator<>(first, second);
	}

	@SafeVarargs
	public static <T> FluentIterator<T> concat(Iterator<T>... first) {
		return first == null ? EmptyIterator.instance() : concat(new ArrayIterator<>(first));
	}

	public static <T> FluentIterator<T> concat(Iterator<? extends Iterator<T>> source) {
		return foldLeft(source, EmptyIterator.instance(), FluentIterators::concat);
	}

	public static <In, Out> FluentIterator<Out> map(Iterator<In> source, Function<? super In, ? extends Out> func) {
		return FluentIterator.from(source::hasNext, Func.lift(source::next).andThen(func));
	}

	public static <In, Out> FluentIterator<Out> flatMap(Iterator<In> source, Function<? super In, ? extends Iterator<Out>> func) {
		return concat(map(source, func));
	}

	public static <T> FluentIterator<T> filter(Iterator<T> source, Function<? super T, Boolean> func) {
		return flatMap(source, item -> {

			if(func.apply(item))
				return new SingletonIterator<>(item);
			else
				return EmptyIterator.instance();
		});
	}

	public static <T> FluentIterator<T> peek(Iterator<T> source, Consumer<? super T> func) {
		return FluentIterator.from(source::hasNext, () -> {
			T next = source.next();
			func.accept(next);
			return next;
		});
	}

	public static <Left, Right> FluentIterator<Tuple2<Left, Right>> zip(Iterator<Left> left, Iterator<Right> right) {
		return FluentIterator.from(() -> left.hasNext() && right.hasNext(), () -> Tuple.of(left.next(), right.next()));
	}

	public static <T> FluentIterator<Tuple2<T, Integer>> zipWithIndex(Iterator<T> source) {
		return new FluentIterator<Tuple2<T, Integer>>() {

			int index = 0;

			@Override
			public boolean hasNext() {
				return source.hasNext();
			}

			@Override
			public Tuple2<T, Integer> next() {
				return Tuple.of(source.next(), index++);
			}
		};
	}

	public static <In, Out> FluentIterator<Out> cast(Iterator<In> source, Class<Out> type) {
		return map(source, type::cast);
	}

	public static <In, Out> FluentIterator<Out> castFiltered(Iterator<In> source, Class<Out> type) {
		return map(filter(source, type::isInstance), type::cast);
	}

	public static <T> FluentIterator<T> take(Iterator<T> source, int num) {
		return new FluentIterator<T>() {

			int size = num;

			@Override
			public boolean hasNext() {
				return size > 0 && source.hasNext();
			}

			@Override
			public T next() {
				size--;
				return source.next();
			}
		};
	}

	public static <T> FluentIterator<T> takeWhile(Iterator<T> source, Function<? super T, Boolean> func) {
		return flatMap(source, new Func<T, Iterator<T>>() {

			boolean done = false;

			@Override
			public Iterator<T> checkedApply(T t) throws Exception {

				if(done)
					return EmptyIterator.instance();

				done = !func.apply(t);

				if(done)
					return EmptyIterator.instance();
				else
					return new SingletonIterator<>(t);
			}
		});
	}

	public static <T> Iterator<T> drop(Iterator<T> source, int num) {
		while(num > 0 && source.hasNext()) {
			num--;
			source.next();
		}

		return source;
	}

	public static <T> Iterator<T> dropWhile(Iterator<T> source, Function<? super T, Boolean> func) {
		Iterator<T> result = EmptyIterator.instance();

		while(source.hasNext()) {
			T next = source.next();

			if(!func.apply(next)) {
				result = concat(new SingletonIterator<>(next), source);
				break;
			}
		}

		return result;
	}

	public static <T> FluentIterator<T> cycle(Iterator<T> source) {
		LazyList<T> items = LazyList.from(source);

		if(items.isEmpty())
			throw new IllegalArgumentException("Can't cycle an empty Iterator!");

		return new FluentIterator<T>() {

			Iterator<T> in = items.iterator();

			@Override
			public boolean hasNext() {
				if(!in.hasNext())
					in = items.iterator();
				return true;
			}

			@Override
			public T next() {
				return in.next();
			}
		};
	}

	public static <T> FluentIterator<T> cycle(Iterator<T> source, int times) {
		LazyList<T> items = LazyList.from(source);

		if(items.isEmpty())
			throw new IllegalArgumentException("Can't cycle an empty Iterator!");

		return new FluentIterator<T>() {

			Iterator<T> in = items.iterator();
			int size = times;

			@Override
			public boolean hasNext() {
				if(!in.hasNext()) {
					if(size == 0)
						return false;

					size--;
					in = items.iterator();
				}
				return true;
			}

			@Override
			public T next() {
				return in.next();
			}
		};
	}

	private static <In, Out> Out foldLeft(Iterator<In> source, Out start, BiFunction<? super Out, ? super In, ? extends Out> func) {
		Out result = start;

		while(source.hasNext()) {
			result = func.apply(result, source.next());
		}

		return result;
	}


}
