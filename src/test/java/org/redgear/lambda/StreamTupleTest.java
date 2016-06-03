package org.redgear.lambda;

import org.redgear.lambda.function.Consumer2;
import java.util.Map;
import static org.redgear.lambda.GenericUtils.*;

/**
 * Created by dcallis on 1/11/2016.
 */
public class StreamTupleTest {

	public void test() {

		stream(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
				.map(i -> tuple(i, i + 1))
				.map(func((i, j) -> i + j));

		Map<String, String> map = map(
			"Test", "1",
			"ABC", "   ",
			"ASDF", "00dfds",
			"qwerty", "<>");


		seq(map).map(func((key, value) -> key + value));


		seq(map).forEach(Consumer2.lift((String key, String value) -> System.out.println(key + value)));
	}


}
