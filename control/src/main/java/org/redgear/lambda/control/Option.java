package org.redgear.lambda.control;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dcallis on 11/23/2015.
 */
public interface Option<Type> extends Iterable<Type> {

	static <Type> Option<Type> option(Type value) {
		return value == null ? none() : some(value);
	}

	static <Type> Option<Type> option(Optional<Type> op) {
		return op.isPresent() ? some(op.get()) : none();
	}

	static <Type> Option<Type> some(Type value) {
		return new Some<>(Objects.requireNonNull(value, "Cannot pass null to Option.some()."));
	}

	static <Type> Option<Type> none() {
		return None.empty();
	}

	boolean isPresent();

	default boolean isEmpty() {
		return !isPresent();
	}

	<Next> Option<Next> map(Function<? super Type, ? extends Next> func);

	<Next> Option<Next> flatMap(Function<? super Type, ? extends Option<Next>> func);

	Option<Type> filter(Function<? super Type, ? extends Boolean> func);

	void forEach(Consumer<? super Type> func);

	default void onPresent(Consumer<? super Type> func) {
		forEach(func);
	}

	default Option<Type> peek(Consumer<? super Type> func) {
		forEach(func);
		return this;
	}

	Option<Type> orElse(Supplier<Option<Type>> func);

	Type orElse(Type alt);

	<E extends Exception> Type orElseThrow(Supplier<E> func) throws E;

	Type orNull();

	void orElseDo(Supplier<?>  func);

	Type get();

	Type getOrElse(Supplier<Type> func);

	Type getOrElse(Type alt);

	Type getOrThrow(Supplier<Exception> func) throws Exception;

	class Some<Type> implements Option<Type> {

		private final Type value;

		public Some(Type value){
			this.value = value;
		}

		@Override
		public boolean isPresent() {
			return true;
		}

		@Override
		public <Next> Option<Next> map(Function<? super Type, ? extends Next> func) {
			return new Some<>(func.apply(value));
		}

		@Override
		public <Next> Option<Next> flatMap(Function<? super Type, ? extends Option<Next>> func) {
			return func.apply(value);
		}

		@Override
		public Option<Type> filter(Function<? super Type, ? extends Boolean> func) {
			return func.apply(value) ? this : None.empty();
		}

		@Override
		public Iterator<Type> iterator() {
			return Collections.singleton(value).iterator();
		}

		@Override
		public void forEach(Consumer<? super Type> func) {
			func.accept(value);
		}

		@Override
		public Some<Type> orElse(Supplier<Option<Type>> func) {
			return this;
		}

		@Override
		public Type orElse(Type alt) {
			return value;
		}

		@Override
		public <E extends Exception> Type orElseThrow(Supplier<E> func) throws E {
			return value;
		}

		@Override
		public Type orNull() {
			return value;
		}

		@Override
		public void orElseDo(Supplier<?>  func) {

		}

		@Override
		public Type get() {
			return value;
		}

		@Override
		public Type getOrElse(Supplier<Type> func) {
			return value;
		}

		@Override
		public Type getOrElse(Type alt) {
			return value;
		}

		@Override
		public Type getOrThrow(Supplier<Exception> func) throws Exception{
			return value;
		}

		@Override
		public String toString() {
			return "Some[" + value + "]";
		}
	}

	class None<Type> implements Option<Type> {

		private static final None<?> empty = new None<>();

		@SuppressWarnings("unchecked")
		public static <Type> None<Type> empty() {
			return (None<Type>) empty;
		}

		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public <Next> Option<Next> map(Function<? super Type, ? extends Next> func) {
			return empty();
		}

		@Override
		public <Next> Option<Next> flatMap(Function<? super Type, ? extends Option<Next>> func) {
			return empty();
		}

		@Override
		public Option<Type> filter(Function<? super Type, ? extends Boolean> func) {
			return empty();
		}

		@Override
		public Iterator<Type> iterator() {
			return Collections.emptyIterator();
		}

		@Override
		public void forEach(Consumer<? super Type> func) {

		}

		@Override
		public Option<Type> orElse(Supplier<Option<Type>> func) {
			return func.get();
		}

		@Override
		public Type orElse(Type alt) {
			return alt;
		}

		@Override
		public <E extends Exception> Type orElseThrow(Supplier<E> func) throws E {
			throw func.get();
		}

		@Override
		public Type orNull() {
			return null;
		}

		@Override
		public void orElseDo(Supplier<?> func) {
			func.get();
		}

		@Override
		public Type get() {
			throw new NoSuchElementException("Attempt to call get() on None.");
		}

		@Override
		public Type getOrElse(Supplier<Type> func) {
			return func.get();
		}

		@Override
		public Type getOrElse(Type alt) {
			return alt;
		}

		@Override
		public Type getOrThrow(Supplier<Exception> func) throws Exception {
			throw func.get();
		}

		@Override
		public String toString() {
			return "None";
		}
	}



}
