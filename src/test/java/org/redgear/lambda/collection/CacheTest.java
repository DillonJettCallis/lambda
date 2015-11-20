package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.Cache;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by dcallis on 7/28/2015.
 *
 */
public class CacheTest {

	private static final Logger log = LoggerFactory.getLogger(CacheTest.class);

	@Test
	public void functionTest(){
		Cache<Double, Double> squareFunc = new Cache<>(d -> {
			log.debug("Calculating ... {}", d);
			return Math.sqrt(d);
		});

		BinaryOperator<Double> sum = (f, s) -> f + s;

		log.debug("First: 0-20");
		IntStream.range(0, 20).mapToObj(Double::valueOf).map(squareFunc).reduce(sum);
		log.debug("Second: 10-50");
		IntStream.range(10, 50).mapToObj(Double::valueOf).map(squareFunc).reduce(sum);
		log.debug("Third: 0-100");
		IntStream.range(0, 100).mapToObj(Double::valueOf).map(squareFunc).reduce(sum);

		assertEquals(100, squareFunc.size());
	}

	public class Recursive<I> {
		public I func;
	}


	@Test
	public void recursiveTest(){
		Cache<Long, Long> triangleNumbers = new Cache<>(null);

		triangleNumbers.mapper = i -> {
			log.debug("Calculating ... {}", i);

			if(i == 0 || i == 1)
				return 1L;
			else
				return triangleNumbers.apply(i - 1L) + triangleNumbers.apply(i - 2L);
		};


		BinaryOperator<Long> sum = (f, s) -> f + s;

		log.debug("First: 0-20");
		IntStream.range(0, 20).mapToObj(Long::valueOf).map(triangleNumbers).reduce(sum);
		log.debug("Second: 10-50");
		IntStream.range(10, 50).mapToObj(Long::valueOf).map(triangleNumbers).reduce(sum);
		log.debug("Third: 0-70");
		IntStream.range(0, 70).mapToObj(Long::valueOf).map(triangleNumbers).reduce(sum);

		assertEquals(70, triangleNumbers.size());
	}

}
