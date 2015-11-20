package org.redgear.lambda;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class Slync {


	public static abstract class Join<Left, Right> {

		protected abstract Stream<Left> getLeft();

		protected abstract Stream<Right> getRight();



		private <Out> Stream<Out> onImpl(Function<Left, Predicate<Right>> checker, BiFunction<Left, Right, Out> mapper) {
			Objects.requireNonNull(checker, "checker is null");
			Objects.requireNonNull(mapper, "joiner is null");

			Iterator<Left> leftSource = getLeft().iterator();

			return StreamSupport.stream(new Spliterators.AbstractSpliterator<Out>(Long.MAX_VALUE, 0) {
				@Override
				public boolean tryAdvance(Consumer<? super Out> consumer) {
					if (leftSource.hasNext()) {
						Left left = leftSource.next();
						Predicate<Right> leftCheck = checker.apply(left);

						Optional<Out> result = getRight()
								.filter(leftCheck::test)
								.findFirst()
								.map(right -> mapper.apply(left, right));

						if (result.isPresent()) {
							consumer.accept(result.get());
							return true;
						} else {
							return tryAdvance(consumer);
						}
					} else
						return false;
				}
			}, false);
		}

		public <Out> Stream<Out> on(BiPredicate<Left, Right> checker, BiFunction<Left, Right, Out> mapper) {
			return onImpl(left -> right -> checker.test(left, right), mapper);
		}

		public <Out> Stream<Out> on(Function<Left, Right> transformer, BiFunction<Left, Right, Out> mapper) {
			return onImpl(left -> transformer.apply(left)::equals, mapper);
		}
	}


	public static <Left, Right> Join<Left, Right> innerJoin(Stream<Left> leftStream, Stream<Right> rightStream) {
		return new Join<Left, Right>(){

			public Stream<Left> getLeft(){
				return StreamUtils.orEmpty(leftStream);
			}

			public Stream<Right> getRight(){
				return StreamUtils.lazy(StreamUtils.orEmpty(rightStream));
			}
		};
	}

	public static <Left, Right> Join<Left, Right> innerJoin(Collection<Left> leftStream, Collection<Right> rightStream) {
		return innerJoin(StreamUtils.stream(leftStream), StreamUtils.stream(rightStream));
	}
}
