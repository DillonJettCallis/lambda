package org.redgear.lambda.concurent;

import org.junit.Test;
import org.redgear.lambda.concurent.proc.Processor;
import org.redgear.lambda.concurent.proc.ProcessorBuilder;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by dcallis on 5/16/2016.
 */
public class ProcessorTest {


	@Test
	public void test() {
		Processor<Integer> p = ProcessorBuilder.builder((Function<Integer, String>) Object::toString)
				.map((String s) -> s.split(""))
				.map(Arrays::stream)
				.map(s -> s.collect(Collectors.joining()))
				.map(Integer::parseInt)
				.map(i -> {System.out.println(i); return i;})
				.build();

		p.process(1);
		p.process(2);
		p.process(3);
		p.process(4, 5, 6, 7, 8, 9, 10);
		p.process(11);
		p.process(12);
		p.process(13);
	}

}
