package org.redgear.lambda;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class CopyableTest {

	@Test
	public void voidCopyTest(){
		Thing thing1 = new Thing(6);
		Thing thing2 = thing1.copy();

		assertFalse(thing1 == thing2);
		assertTrue(thing1.equals(thing2));
	}


	private static class Thing implements Copyable<Thing> {

		final int data;

		public Thing(int data){
			this.data = data;
		}

		@Override
		public Thing copy() {
			return new Thing(data);
		}

		@Override
		public boolean equals(Object other){
			if(other instanceof Thing) {
				Thing th = (Thing) other;
				return data == th.data;
			}
			else
				return false;
		}
	}
}
