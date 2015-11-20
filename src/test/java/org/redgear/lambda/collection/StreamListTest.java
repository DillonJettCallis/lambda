package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.StreamListImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class StreamListTest {

	private static final Logger log = LoggerFactory.getLogger(StreamListTest.class);

	@Test
	public void basicMapTest() {
		final int size = 8;

		StreamList<Integer> streamList = StreamListImpl.generate((int i) -> "Test " + i, size)
				.map(s -> s.split(" ")[1])
				.map(Integer::parseInt);

		log.debug("Result: Size: {}, Value: {}", streamList.size(), streamList.toArray());

		for (int i = 0; i < size; i++) {
			assertEquals(i, streamList.get(i).intValue());
		}
	}

	@Test
	public void basicFilterTest() {
		StreamList<Integer> streamList = StreamList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.filter(this::isEven).reduce(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapOpTest() {
		StreamList<Integer> streamList = StreamList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMapOp(i -> i % 2 == 0 ? Optional.of(i) : Optional.empty())
				.reduce(0, (f, s) -> f + s);

		assertEquals(30, result);
	}

	@Test
	public void basicFlatMapItTest(){
		StreamList<Number> streamList = StreamList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		int result = streamList.flatMapIt(this::constList)
				.filter(this::isEven)
				.reduce(0, (f, s) -> f + s);

		assertEquals(330, result);
	}

	@Test
	public void subListTest(){
		StreamList<Integer> list = StreamList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		StreamList<Integer> subList = list.subList(3, 6);

		assertEquals(StreamList.from(3, 4, 5), subList);
	}

	private boolean isEven(int i){
		return i % 2 == 0;
	}


	private List<Integer> constList(Number i) {
		return StreamList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}
}