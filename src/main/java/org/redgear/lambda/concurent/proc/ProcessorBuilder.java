package org.redgear.lambda.concurent.proc;

import java.util.function.Function;

/**
 * Created by dcallis on 5/13/2016.
 */
public interface ProcessorBuilder<In, Mid, Out> {

	default Processor<In> build() {
		return head();
	}

	Processor<In> head();

	Out accept(Mid mid);

	static <In, Out> ProcessorBuilder<In, In, Out> builder(Function<? super In, ? extends Out> func) {

		return new ProcessorBuilder<In, In, Out>() {

			@Override
			public Processor<In> head() {
				return new Processor<In>() {
					@Override
					public void process(In in) {
						accept(in);
					}
				};
			}

			@Override
			public Out accept(In in) {
				return func.apply(in);
			}
		};
	}


	default <Result> ProcessorBuilder<In, Out, Result> map(Function<? super Out, ? extends Result> func) {
		Processor<In> head = head();

		return new ProcessorBuilder<In, Out, Result>() {
			@Override
			public Processor<In> head() {
				return new Processor<In>() {
					@Override
					public void process(In in) {
						head.process(in);
					}
				};
			}

			@Override
			public Result accept(Out mid) {
				return func.apply(mid);
			}
		};
	}


}
