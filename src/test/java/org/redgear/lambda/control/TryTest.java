package org.redgear.lambda.control;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dcallis on 3/17/2016.
 */
public class TryTest {



	@Test
	public void successTest() {

		String result = Try.of(() -> {
			Thread.sleep(500);
			return "Success";
		}).get();


		Assert.assertEquals("Success", result);

	}


	@Test
	public void failureTest() {

		String result = Try.<String>of(() -> {

			//Do other things
			throw new RuntimeException("Failure");

		}).recover(Throwable::getMessage).get();

		Assert.assertEquals("Failure", result);
	}

	@Test
	public void mapTest() {


		String result = Try.of(() -> "Success")
				.map(String::toLowerCase)
				.<String>map((String s) -> {
					throw new RuntimeException("Failure");
				}).recover(Throwable::getMessage)
				.get();

		Assert.assertEquals("Failure", result);

	}

}
