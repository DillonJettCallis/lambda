package org.redgear.lambda.control;

import org.redgear.lambda.function.Func;

import java.util.NoSuchElementException;

/**
 * Created by dcallis on 11/23/2015.
 */
public interface Either<L, R> {


	boolean isLeft();

	default boolean isRight() {
		return ! isLeft();
	}

	default LeftProjection<L, R> left() {
		return new LeftProjection<>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<>(this);
	}

	L getLeft();

	R getRight();

	static <L, R> Either<L, R> left(L value) {
		return new Left<>(value);
	}

	static <L, R> Either<L, R> right(R value) {
		return new Right<>(value);
	}

	class Left<L, R> implements Either<L, R> {

		private final L value;

		public Left(L value) {
			this.value = value;
		}

		@Override
		public boolean isLeft() {
			return true;
		}

		@Override
		public L getLeft() {
			return value;
		}

		@Override
		public R getRight() {
			throw new NoSuchElementException("Attempt to call getRight() on Either.Left");
		}
	}

	class Right<L, R> implements Either<L, R> {

		private final R value;

		public Right(R value){
			this.value = value;
		}

		@Override
		public boolean isLeft() {
			return false;
		}

		@Override
		public L getLeft() {
			throw new NoSuchElementException("Attempt to call getLeft() on Either.Right");
		}

		@Override
		public R getRight() {
			return value;
		}

	}

	abstract class Projection<L, R> {

		protected final Either<L, R> either;

		public Projection(Either<L, R> either){
			this.either = either;
		}


		public L asLeft() {
			return ((Left<L, R>) either).value;
		}

		public R asRight() {
			return ((Right<L, R>) either).value;
		}

	}

	class LeftProjection<L, R> extends Projection<L, R>{

		public LeftProjection(Either<L, R> either){
			super(either);
		}

		@SuppressWarnings("unchecked")
		public <Next> LeftProjection<Next, R> map(Func<L, Next> func){
			if(either.isLeft()){
				return new Left<Next, R>(func.apply(asLeft())).left();
			} else {
				return (LeftProjection<Next, R>) this;
			}
		}

		@SuppressWarnings("unchecked")
		public <Next> LeftProjection<Next, R> flatMap(Func<L, Either<Next, R>> func){
			if(either.isLeft()){
				return func.apply(asLeft()).left();
			} else {
				return (LeftProjection<Next, R>) this;
			}
		}

		public Option<L> filter(Func<L, Boolean> func) {
			if(either.isLeft() && func.apply(asLeft())){
				return Option.some(asLeft());
			} else {
				return Option.none();
			}
		}

		public Option<L> toOption() {
			if(either.isLeft()){
				return Option.some(asLeft());
			} else {
				return Option.none();
			}
		}

		public void forEach(Func<L, ?> func) {
			if(either.isLeft())
				func.apply(asLeft());
		}

		public L get() {
			if(either.isLeft())
				return asLeft();
			else {
				throw new NoSuchElementException("Attempt to call get() on an empty Either LeftProjection");
			}
		}
	}

	class RightProjection<L, R> extends Projection<L, R>{

		public RightProjection(Either<L, R> either){
			super(either);
		}

		@SuppressWarnings("unchecked")
		public <Next> RightProjection<L, Next> map(Func<R, Next> func){
			if(either.isRight()){
				return new Right<L, Next>(func.apply(asRight())).right();
			} else {
				return (RightProjection<L, Next>) this;
			}
		}

		@SuppressWarnings("unchecked")
		public <Next> RightProjection<L, Next> flatMap(Func<R, Either<L, Next>> func){
			if(either.isRight()){
				return func.apply(asRight()).right();
			} else {
				return (RightProjection<L, Next>) this;
			}
		}

		public Option<R> filter(Func<R, Boolean> func) {
			if(either.isRight() && func.apply(asRight())){
				return Option.some(asRight());
			} else {
				return Option.none();
			}
		}

		public Option<R> toOption() {
			if(either.isRight()){
				return Option.some(asRight());
			} else {
				return Option.none();
			}
		}

		public void forEach(Func<R, ?> func) {
			if(either.isRight())
				func.apply(asRight());
		}

		public R get() {
			if(either.isRight())
				return asRight();
			else {
				throw new NoSuchElementException("Attempt to call get() on an empty Either RightProjection");
			}
		}
	}

}
