package org.redgear.lambda.control;

import org.junit.Assert;
import org.junit.Test;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple3;

/**
 * Created by LordBlackHole on 6/18/2016.
 */
public class TrampolineTest {





	@Test
	public void fibonacciTest() {
		Assert.assertEquals(650_574_555, fibonacci(400));
	}

	/**
	 * The public facing code that hides the recursion and trampoline.
	 */
	private int fibonacci(int index) {
		Trampoline<Tuple3<Integer, Integer, Integer>, Integer> trampoline = Trampoline.of(this::fibonacci);

		return trampoline.apply(Tuple.of(0, 1, index));
	}

	/**
	 * The private recursive impl.
	 */
	private Either<Tuple3<Integer, Integer, Integer>, Integer> fibonacci(Tuple3<Integer, Integer, Integer> args) {
		int first = args.v1;
		int second = args.v2;
		int index = args.v3;

		if(index == 0)
			return Either.right(first);
		else
			return Either.left(Tuple.of(second, first + second, --index));
	}

}
