package org.redgear.lambda;

import org.junit.Test;
import org.redgear.lambda.collection.ImmutableList;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.collection.StreamList;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.stream.Stream;

import static org.redgear.lambda.GenericUtils.*;

/**
 * Created by dcallis on 4/6/2016.
 */
public class GenericUtilsTest {


	public static Map<String, String> testMap() {
		Map<String, String> map = new HashMap<>();

		map.put("A", "B");
		map.put("C", "D");
		map.put("E", "F");

		return map;
	}

	@Test
	public void iteratorTest() {
		Iterator<String> empty = iterator();

		Iterator<String> array = iterator("A", "B", "C");

		Iterator<String> iterator = iterator(Arrays.asList("A", "B", "C").iterator());

		Iterator<String> iterable = iterator((Iterable<String>) Arrays.asList("A", "B", "C"));

		Iterator<String> collection = iterator((Collection<String>) Arrays.asList("A", "B", "C"));

		Iterator<String> list = iterator(Arrays.asList("A", "B", "C"));

		Iterator<String> set = iterator(new HashSet<>(Arrays.asList("A", "B", "C")));

		Iterator<String> stream = iterator(Stream.of("A", "B", "C"));

		Iterator<Tuple2<String, String>> map = iterator(testMap());

		Iterator<String> seq = iterator(Seq.from("A", "B", "C"));

		Iterator<String> streamList = iterator(StreamList.from("A", "B", "C"));

		Iterator<String> immutableList = iterator(ImmutableList.from("A", "B", "C"));
	}


}
