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

	class Left<L, R> implements Either<L, R> {

		private final L value;

		public Left(L value) {
			this.value = value;
		}

		@Override
		public boolean isLeft() {
			return true;
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

	}

	abstract class Projection<L, R> {

		protected final Either<L, R> either;

		public Projection(Either<L, R> either){
			this.either = either;
		}


		L asLeft() {
			return ((Left<L, R>) either).value;
		}

		R asRight() {
			return ((Right<L, R>) either).value;
		}

	}

	class LeftProjection<L, R> extends Projection<L, R>{

		public LeftProjection(Either<L, R> either){
			super(either);
		}

		@SuppressWarnings("unchecked")
		<Next> LeftProjection<Next, R> map(Func<L, Next> func){
			if(either.isLeft()){
				return new Left<Next, R>(func.apply(asLeft())).left();
			} else {
				return (LeftProjection<Next, R>) this;
			}
		}

		@SuppressWarnings("unchecked")
		<Next> LeftProjection<Next, R> flatMap(Func<L, Either<Next, R>> func){
			if(either.isLeft()){
				return func.apply(asLeft()).left();
			} else {
				return (LeftProjection<Next, R>) this;
			}
		}

		Option<L> filter(Func<L, Boolean> func) {
			if(either.isLeft() && func.apply(asLeft())){
				return Option.some(asLeft());
			} else {
				return Option.none();
			}
		}

		Option<L> toOption() {
			if(either.isLeft()){
				return Option.some(asLeft());
			} else {
				return Option.none();
			}
		}

		void forEach(Func<L, ?> func) {
			if(either.isLeft())
				func.apply(asLeft());
		}
	}

	class RightProjection<L, R> extends Projection<L, R>{

		public RightProjection(Either<L, R> either){
			super(either);
		}

		@SuppressWarnings("unchecked")
		<Next> RightProjection<L, Next> map(Func<R, Next> func){
			if(either.isRight()){
				return new Right<L, Next>(func.apply(asRight())).right();
			} else {
				return (RightProjection<L, Next>) this;
			}
		}

		@SuppressWarnings("unchecked")
		<Next> RightProjection<L, Next> flatMap(Func<R, Either<L, Next>> func){
			if(either.isRight()){
				return func.apply(asRight()).right();
			} else {
				return (RightProjection<L, Next>) this;
			}
		}

		Option<R> filter(Func<R, Boolean> func) {
			if(either.isRight() && func.apply(asRight())){
				return Option.some(asRight());
			} else {
				return Option.none();
			}
		}

		Option<R> toOption() {
			if(either.isRight()){
				return Option.some(asRight());
			} else {
				return Option.none();
			}
		}

		void forEach(Func<R, ?> func) {
			if(either.isRight())
				func.apply(asRight());
		}
	}

}
