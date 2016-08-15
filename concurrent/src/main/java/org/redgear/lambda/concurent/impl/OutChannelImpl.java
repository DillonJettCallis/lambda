package org.redgear.lambda.concurent.impl;

import org.redgear.lambda.concurent.OutChannel;
import org.redgear.lambda.control.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by LordBlackHole on 8/14/2016.
 */
public class OutChannelImpl<In, Out> implements OutChannel<Out> {

	private final OutChannel<In> source;
	private final Function<? super In, ? extends Iterable<? extends Out>> func;
	private final List<Out> buffer = new ArrayList<>();

	public OutChannelImpl(OutChannel<In> source, Function<? super In, ? extends Iterable<? extends Out>> func) {
		this.source = source;
		this.func = func;
	}


	private Option<Out> next(Supplier<Option<In>> source) {
		//Loop until we have an element or source returns none. (Avoids recursion).
		while(true) {
			if (buffer.isEmpty()) {
				Option<In> input = source.get();

				if (input.isEmpty())
					return Option.none();
				else {
					Iterable<? extends Out> output = func.apply(input.get());

					if (output != null)
						output.forEach(buffer::add);
				}
			} else {
				return Option.option(buffer.remove(0));
			}
		}
	}


	@Override
	public Option<Out> poll() {
		return next(source::poll);
	}

	@Override
	public Option<Out> poll(long timeOut, TimeUnit unit) {
		return next(() -> source.poll(timeOut, unit));
	}

	@Override
	public Out take() throws InterruptedException {
		class BypassException extends RuntimeException {
			private InterruptedException ex;
		}

		try {
			return next(() -> {

				try {
					return Option.option(source.take());
				} catch (InterruptedException e) {
					BypassException ex = new BypassException();
					ex.ex = e;
					throw ex;
				}

			}).get();
		} catch (BypassException e) {
			throw e.ex;
		}
	}

	public <R> OutChannel<R> map(Function<? super Out, ? extends R> func) {
		return new OutChannelImpl<>(this, func.andThen(Collections::singleton));
	}

	public OutChannel<Out> filter(Function<? super Out, ? extends Boolean> func) {
		return new OutChannelImpl<>(this, in -> func.apply(in) ? Collections.singleton(in) : Collections.emptyList());
	}

	public <R> OutChannel<R> flatMap(Function<? super Out, ? extends Iterable<? extends R>> func) {
		return new OutChannelImpl<>(this, func);
	}

}
