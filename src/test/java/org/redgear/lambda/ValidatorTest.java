package org.redgear.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class ValidatorTest {

	@Test
	public void testNotEmptyCollection(){
		List<String> list = new LinkedList<>();
		list.add("Item");

		List<String> tested = Validator.notEmpty(list);
	}

	@Test
	public void testIsEmptyCollection(){
		String message = "Test";
		List<String> list = new LinkedList<>();

		try {
			List<String> tested = Validator.notEmpty(list, message);
			fail("Validator should have thrown exception!");
		} catch (ValidationException e){
			assertEquals(message, e.getMessage());
		}
	}

	@Test
	public void testPredicate(){
		Validator.validate("Test", s -> s.equals("Test"));
	}

	@Test
	public void testNullPredicate(){
		String message = "Test";
		try {
			Validator.validate(null, s -> s.equals("Test"), message);
			fail("Validator should have thrown exception!");
		} catch (ValidationException e){
			assertEquals(message, e.getMessage());
		}
	}

	@Test
	public void testNotBlank(){
		Validator.notBlank(" test  ");
	}

	@Test
	public void testBlank(){
		String message = "Test";
		try {
			Validator.notBlank(" \n   \t  \r  ", message);
			fail("Validator should have thrown exception!");
		} catch (ValidationException e){
			assertEquals(message, e.getMessage());
		}
	}

	@Test
	public void testNotBlankAndTrim(){
		Assert.assertEquals("test", Validator.notBlankAndTrim("  test  "));
	}
}
