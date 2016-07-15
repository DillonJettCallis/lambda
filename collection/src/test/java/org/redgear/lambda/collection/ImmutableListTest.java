package org.redgear.lambda.collection;

import org.junit.Test;
import org.redgear.lambda.GenericUtils;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Consumer1;
import org.redgear.lambda.function.Func2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

		ImmutableList<Integer> streamList = StreamBuilder.from(i -> "Test " + i, size).seq().toImmutableList()
				.map(s -> s.split(" ")[1])
				.map(Integer::parseInt);

		log.debug("Result: Value: {}", streamList);

		for (int i = 0; i < size; i++) {
			assertEquals(i, streamList.head().intValue());

			streamList = streamList.tail();
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
		ImmutableList<Integer> streamList = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMap(this::constList)
				.filter(this::isEven)
				.fold(0, (f, s) -> f + s);

		assertEquals(330, result);
	}

	@Test
	public void subListTest(){
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> subList = list.slice(3, 6);

		assertEquals(ImmutableList.from(3, 4, 5), subList);
	}

	@Test
	public void lastTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(10, list.last().intValue());
	}

	@Test
	public void lastOptionTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(10, list.lastOption().get().intValue());
	}

	@Test
	public void initTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list.init());
	}

	@Test
	public void concatItTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> concat = list.concat(new SingletonIterator<>(11));

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatStreamTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> concat = list.concat(Stream.of(11));

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatSingleTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> concat = list.concat(11);

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatSeqTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> concat = list.concat(Seq.from(11));

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatStreamListTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> concat = list.concat(StreamList.from(11));

		assertEquals(ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void foldRightTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertEquals(55, list.foldRight(0, (l, r) -> l + r).intValue());
	}


	@Test
	public void reduceTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertEquals(55, list.reduce((l, r) -> l + r).intValue());
	}

	@Test
	public void forAnyTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertTrue(list.forAny(i -> i == 10));
	}

	@Test
	public void forAllTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertTrue(list.forAll(i -> i <= 10));
	}

	@Test
	public void reverseTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reverse();

		ImmutableList<Integer> reverse = ImmutableList.from(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);

		assertEquals(reverse, list);
	}

	@Test
	public void zipTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> reverse = list.reverse();

		assertTrue(reverse.zip(list).map(Func2.lift((l, r) -> l + r)).forAll(i -> i == 10));
	}

	@Test
	public void notEqualsTest() {
		ImmutableList<Integer> list = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		ImmutableList<Integer> reverse = list.reverse();

		assertFalse(list.equals(reverse));
	}

	@Test
	public void hashCodeTest() {
		int firstCode = ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).hashCode();

		int secondCode = GenericUtils.list(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).hashCode();

		assertEquals(secondCode, firstCode);
	}

	private boolean isEven(int i){
		return i % 2 == 0;
	}


	private ImmutableList<Integer> constList(Number i) {
		return ImmutableList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

}
