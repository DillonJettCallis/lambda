package org.redgear.lambda.collection;

import org.junit.Test;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Consumer1;
import org.redgear.lambda.function.Func2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by dcallis on 11/27/2015.
 */
public class ImmutableListTest {

	private static final Logger log = LoggerFactory.getLogger(ImmutableListTest.class);


	@Test
	public void func2Test() {
		ImmutableList.Nil.<Integer>nil().prepend(0).prepend(1).prepend(2).prepend(3)
				.zipWithIndex()
				.map(Func2.lift((Integer f, Integer s) -> f + s))
				.forEach(System.out::println);


	}

	@Test
	public void basicMapTest() {
		final int size = 8;

		StreamList<Integer> streamList = StreamBuilder.from(i -> "Test " + i, size).seq().toImmutableList()
				.map(s -> s.split(" ")[1])
				.map(Integer::parseInt)
				.toStreamList();

		log.debug("Result: Size: {}, Value: {}", streamList.size(), streamList.toArray());

		for (int i = 0; i < size; i++) {
			assertEquals(i, streamList.get(i).intValue());
		}
	}

	@Test
	public void basicFilterTest() {
		ImmutableList<Integer> streamList = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.filter(this::isEven).fold(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapOpTest() {
		ImmutableList<Integer> streamList = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMap(i -> i % 2 == 0 ? Option.some(i) : Option.none())
				.fold(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapItTest(){
		ImmutableList<Number> streamList = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMap(this::constList)
				.filter(this::isEven)
				.fold(0, (f, s) -> f + s);

		assertEquals(330, result);
	}

	@Test
	public void subListTest(){
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> subList = list.slice(3, 6);

		assertEquals(ImmutableList.from(3, 4, 5).toList(), subList.toList());
	}

	private boolean isEven(int i){
		return i % 2 == 0;
	}


	private ImmutableList<Integer> constList(Number i) {
		return ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

}
