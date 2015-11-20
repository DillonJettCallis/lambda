package org.redgear.lambda;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.redgear.lambda.Identity.equalsCalculator;
import static org.redgear.lambda.Identity.hashcode;
import static org.redgear.lambda.Initializer.init;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class IdentityTest {

	private static final Logger log = LoggerFactory.getLogger(IdentityTest.class);



	@Test
	public void testHashcode(){
		Thing thing = new Thing();

		int initialHash = hashcode(thing);

		//Running on the same obj again should of course be the same
		assertEquals(initialHash, hashcode(thing));

		thing.data = 1;

		int dataChangeHash = hashcode(thing);

		log.debug("First hash: {}, Second hash: {}", initialHash, dataChangeHash);

		//changed a value, even with same object hash should differ
		assertNotEquals(initialHash, dataChangeHash);

		int secondInitialHash = hashcode(new Thing());

		//Different object but same value as initial
		assertEquals(initialHash, secondInitialHash);

		thing.innerThing = init(NestedThing::new, n -> n.evenMoreData = "SomethingElse");

		int innerDataChangeHash = hashcode(thing);

		//Changing inner data should change the hash too.
		assertNotEquals(dataChangeHash, innerDataChangeHash);

		Thing thing2 = thing.copy();

		//A deep copy should keep the same hash.
		assertEquals(innerDataChangeHash, hashcode(thing2));
	}


	@Test
	public void testEquals(){
		Thing thing = new Thing();

		Predicate<Object> initialHash = equalsCalculator(thing);

		//Object must be equal to itself
		assertTrue(initialHash.test(thing));

		Thing thing2 = thing.copy();

		//Object is equal to a copy of itself
		assertTrue(initialHash.test(thing2));

		thing2.data = 1;

		Predicate<Object> dataChangeHash = equalsCalculator(thing2);

		//Changing one value should make them not equal
		assertFalse(initialHash.test(thing2));

		log.debug("Test: {}", Identity.toString(thing));
		log.debug("Test: {}", Identity.toString(new Thing()));

		//Different object but same value as initial
		assertTrue(initialHash.test(new Thing()));

		Thing thing3 = thing2.copy();
		thing3.innerThing = init(NestedThing::new, n -> n.evenMoreData = "SomethingElse");

		//Changing inner data should not be equal
		assertFalse(dataChangeHash.test(thing3));
	}

	private static class Thing implements Identity, Copyable<Thing> {

		int data = 0;
		String moreData = "MoreData";
		NestedThing innerThing = new NestedThing();

		public List<Object> identity(){
			return Arrays.asList(data, moreData, innerThing);
		}

		public Optional<List<String>> labels() {
			return Optional.of(Arrays.asList("Data", "MoreData", "Inner Thing"));
		}

		@Override
		public Thing clone() {
			return init(Thing::new, c -> {
				c.data = data;
				c.moreData = moreData;
				c.innerThing = innerThing.clone();
			});
		}

		@Override
		public int hashCode(){
			return hashcode(this);
		}

		@Override
		public String toString(){
			return toString(this);
		}

		@Override
		public boolean equals(Object other){
			return equals(this, other);
		}
	}

	private static class NestedThing implements Identity, Copyable<NestedThing> {

		String evenMoreData = "EvenMoreData";
		double pi = 3.141592659;

		public List<Object> identity(){
			return Arrays.asList(evenMoreData, pi);
		}

		public Optional<List<String>> labels() {
			return Optional.of(Arrays.asList("evenMoreData", "pi"));
		}

		@Override
		public NestedThing clone() {
			return init(NestedThing::new, c -> {
				c.evenMoreData = this.evenMoreData;
				c.pi = this.pi;
			});
		}

		@Override
		public int hashCode(){
			return hashcode(this);
		}

		@Override
		public String toString(){
			return toString(this);
		}

		@Override
		public boolean equals(Object other){
			return equals(this, other);
		}
	}
}
