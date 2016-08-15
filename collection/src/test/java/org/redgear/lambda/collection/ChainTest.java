package org.redgear.lambda.collection;

import org.junit.Test;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Func2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dcallis on 11/27/2015.
 */
public class ChainTest {

	private static final Logger log = LoggerFactory.getLogger(ChainTest.class);


	@Test
	public void func2Test() {
		Chain.<Integer>nil().prepend(0).prepend(1).prepend(2).prepend(3)
				.zipWithIndex()
				.map(Func2.lift((Integer f, Integer s) -> f + s))
				.forEach(System.out::println);


	}

	@Test
	public void basicMapTest() {
		final int size = 8;

		Chain<Integer> streamList = StreamBuilder.from(i -> "Test " + i, size).seq().toChain()
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
		Chain<Integer> streamList = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.filter(this::isEven).fold(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapOpTest() {
		Chain<Integer> streamList = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMap(i -> i % 2 == 0 ? Option.some(i) : Option.none())
				.fold(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapItTest(){
		Chain<Integer> streamList = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		log.info("Chain: {}", streamList);

		int result = streamList.flatMap(this::constList)
				.filter(this::isEven)
				.fold(0, (f, s) -> f + s);

		assertEquals(330, result);
	}

//	@Test
//	public void subListTest(){
//		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//		Chain<Integer> subList = list.slice(3, 6);
//
//		assertEquals(Chain.from(3, 4, 5), subList);
//	}

	@Test
	public void lastTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(10, list.last().intValue());
	}

	@Test
	public void lastOptionTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(10, list.lastOption().get().intValue());
	}

	@Test
	public void initTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		assertEquals(Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list.init());
	}

	@Test
	public void concatItTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> concat = list.concat(SingletonIterator.from(11));

		assertEquals(Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatStreamTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> concat = list.concat(Stream.of(11));

		assertEquals(Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatSingleTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> concat = list.concat(11);

		assertEquals(Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}

	@Test
	public void concatSeqTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> concat = list.concat(Seq.from(11));

		assertEquals(Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), concat);
	}


	@Test
	public void reduceTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertEquals(55, list.reduce((l, r) -> l + r).get().intValue());
	}

	@Test
	public void forAnyTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertTrue(list.forAny(i -> i == 10));
	}

	@Test
	public void forAllTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


		assertTrue(list.forAll(i -> i <= 10));
	}

	@Test
	public void reverseTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reverse();

		Chain<Integer> reverse = Chain.from(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);

		assertEquals(reverse, list);
	}

	@Test
	public void zipTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> reverse = list.reverse();

		assertTrue(reverse.zip(list.iterator()).map(Func2.lift((l, r) -> l + r)).forAll(i -> i == 10));
	}

	@Test
	public void notEqualsTest() {
		Chain<Integer> list = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		Chain<Integer> reverse = list.reverse();

		assertFalse(list.equals(reverse));
	}

	@Test
	public void hashCodeTest() {
		int firstCode = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).hashCode();

		int secondCode = Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10).hashCode();

		assertEquals(secondCode, firstCode);
	}

	private boolean isEven(int i){
		return i % 2 == 0;
	}


	private Chain<Integer> constList(Number i) {
		return Chain.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

}
