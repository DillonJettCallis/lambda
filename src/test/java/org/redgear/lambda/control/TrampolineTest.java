package org.redgear.lambda.control;

import org.junit.Assert;
import org.junit.Test;
import org.redgear.lambda.tuple.Tuple3;

import static org.redgear.lambda.GenericUtils.*;

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
		Trampoline<Tuple3<Integer, Integer, Integer>, Integer> trampoline = Trampoline.of(func3(this::fibonacci));

		return trampoline.apply(tuple(0, 1, index));
	}

	/**
	 * The private recursive impl.
	 */
	private Either<Tuple3<Integer, Integer, Integer>, Integer> fibonacci(Integer first, Integer second, Integer index) {
		if(index == 0)
			return right(first);
		else
			return left(tuple(second, first + second, --index));
	}

}
