package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by dcallis on 1/1/2016.
 */
@FunctionalInterface
public interface StreamBuilder<T> {


	Stream<T> stream();

	default Seq<T> seq() {
		return Seq.from(stream());
	}

	default StreamList<T> streamList() {
		return StreamList.from(stream());
	}


	static <T> StreamBuilder<T> from(Supplier<Option<T>> source) {
		return new SupplierStreamBuilder<>(source);
	}

	static <T> StreamBuilder<T> from(Function<? super T, ? extends Option<T>> mapper, T start) {
		return new AdvancingStreamBuilder<>(mapper, start);
	}

	static <T> StreamBuilder<T> from(Collection<T> source) {
		return source::stream;
	}

	static <T> StreamBuilder<T> from(Iterable<T> source) {
		return from(source.iterator());
	}

	static <T> StreamBuilder<T> from(Iterator<T> source) {
		return () -> StreamSupport.stream(Spliterators.spliterator(source, Long.MAX_VALUE, 0), false);
	}

	static <T> StreamBuilder<T> from(Function<? super Integer, T> mapper, int start, int stop) {
		return new IndexedStreamBuilder<>(mapper, start, stop);
	}

	static <T> StreamBuilder<T> from(Function<? super Integer, T> mapper, int stop) {
		return new IndexedStreamBuilder<>(mapper, 0, stop);
	}

	/**
	 * Constructed with a Supplier<Option<T>> this Builder will call the Supplier using any values that return Some
	 * and ends the stream on the first None.
	 * @param <T>
	 */
	class SupplierStreamBuilder<T> implements StreamBuilder<T>{
		private final Supplier<Option<T>> source;

		public SupplierStreamBuilder(Supplier<Option<T>> source) {
			this.source = source;
		}

		@Override
		public Stream<T> stream() {
			return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {

				@Override
				public boolean tryAdvance(Consumer<? super T> action) {
					Option<T> next = source.get();

					next.forEach(action);

					return next.isPresent();
				}

			}, false);
		}
	}

	/**
	 * Built using a Function<T, Option<T>> and a starting T, this builder will take the previous T and pass it to
	 * the function, then take the result of that and pass it back in until the function returns None.
	 * @param <T>
	 */
	class AdvancingStreamBuilder<T> implements StreamBuilder<T> {

		private final Function<? super T, ? extends Option<T>> mapper;
		private Option<T> next;

		public AdvancingStreamBuilder(Function<? super T, ? extends Option<T>> mapper, T start) {
			this.mapper = mapper;
			this.next = Option.some(start);
		}

		@Override
		public Stream<T> stream() {
			return LazyList.from(new Iterator<T>(){

				@Override
				public boolean hasNext() {
					return next.isPresent();
				}

				@Override
				public T next() {
					Option<T> last = next;

					next = last.flatMap(mapper);

					return last.get();
				}
			}).stream();
		}
	}

	/**
	 * Takes a Function from int to T and calls it passing in each int from start (inclusive) to stop (exclusive).
	 * @param <T>
	 */
	class IndexedStreamBuilder<T> implements StreamBuilder<T> {

		private final Function<? super Integer, T> mapper;
		private final int start;
		private final int stop;

		public IndexedStreamBuilder(Function<? super Integer, T> mapper, int start, int stop) {
			this.mapper = mapper;
			this.start = start;
			this.stop = stop;
		}

		@Override
		public Stream<T> stream() {
			return IntStream.range(start, stop).mapToObj(mapper::apply);
		}
	}

//	class AppendingStreamBuilder<T> implements StreamBuilder<T> {
//
//		private final List<T> results = new ArrayList<>();
//		private final Lock lock = new ReentrantLock();
//		private boolean closed = false;
//		private Promise<Void> next = Promise.promise();
//
//		@Override
//		public Stream<T> stream() {
//			return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
//
//				@Override
//				public boolean tryAdvance(Consumer<? super T> action) {
//					lock.lock();
//
//					if (results.isEmpty()) {
//
//						if (closed) {
//							lock.unlock();
//
//							return false;
//						} else {
//							Promise<Void> next = AppendingStreamBuilder.this.next;
//
//							lock.unlock();
//
//							try {
//								next.future().await(30, TimeUnit.SECONDS);
//							} catch (InterruptedException | ExecutionException | TimeoutException e) {
//								//Not really important.
//							}
//
//							return tryAdvance(action);
//						}
//					} else {
//						action.accept(results.remove(0));
//
//						lock.unlock();
//						return true;
//					}
//				}
//			}, false);
//		}
//
//		public AppendingStreamBuilder<T> add(T next){
//
//			lock.lock();
//
//			if (closed) {
//				lock.unlock();
//
//				return this;
//			}
//
//			results.add(next);
//			advancePromise();
//			lock.unlock();
//
//			return this;
//
//		}
//
//		public void close(){
//			lock.lock();
//			closed = true;
//			advancePromise();
//			lock.unlock();
//		}
//
//		private void advancePromise() {
//			next.success(null);
//			next = Promise.promise();
//		}
//	}


	class BufferedStreamBuilder<T> implements StreamBuilder<T> {

		private final ArrayBlockingQueue<T> queue;
		private boolean closed = false;

		public BufferedStreamBuilder(int bufferSize) {
			queue = new ArrayBlockingQueue<>(bufferSize);
		}

		@Override
		public Stream<T> stream() {
			return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {

				@Override
				public boolean tryAdvance(Consumer<? super T> action) {
					if(closed)
						return false;

					while (true) {
						try {
							T t = queue.poll(1, TimeUnit.SECONDS);

							if(closed)
								return false;

							if(t == null)
								continue;

							action.accept(t);

							return true;
						} catch (InterruptedException e) {
							if(closed)
								return false;
						}
					}
				}
			}, false);
		}

		public void close(){
			closed = true;
		}

		public BufferedStreamBuilder<T> add(T next){
			try {
				queue.offer(next, 10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			return this;
		}
	}
}
