package org.redgear.lambda.control;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dcallis on 4/5/2016.
 */
public class OptionTest {



	@Test
	public void orThrowTest() {


		Assert.assertEquals(Integer.valueOf(0), Option.some(0).orElseThrow(() -> new RuntimeException("Test")));

	}

	@Test
	public void orThrowCheckedTest() {


		try {
			Assert.assertEquals(Integer.valueOf(0), Option.<Integer>none().orElseThrow(() -> new Exception("Test")));
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals("Test", e.getMessage());
		}

	}
}
