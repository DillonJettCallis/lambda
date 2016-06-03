package org.redgear.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by dcallis on 12/9/2015.
 */
public class StreamCheatSheet {

	/**
	 * Helper method for creating Integer Streams.
	 * @param max Max value for stream
	 * @return A stream of values from 0 to max.
	 */
	private Stream<Integer> intStream(int max){
		return IntStream.range(0, max).boxed();
	}

	/**
	 * Map
	 * From:     Stream<A>
	 * Takes:    Function of A -> B
	 * Produces: Stream<B>
	 * By:       Converting every A into a B with the supplied method. (A and B can be the same type, like a map that takes Strings can calls toUpper() on them.)
	 */
	@Test
	public void mapTest(){
		Stream<String> result = intStream(10)
				.map(i -> String.valueOf(i)); //Takes each Integer and converts it to a String.

		Assert.assertEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), result.collect(Collectors.toList()));
	}

	/**
	 * FlatMap
	 * From:     Stream<A>
	 * Takes:    Function of A -> Stream<B>
	 * Produces: Stream<B>
	 * By:       Applies the function to convert every A into a Stream<B>, then concatenates all the B values together in a single Stream.
	 */
	@Test
	public void flatMapTest(){
		Stream<Integer> result = intStream(10)
				.flatMap(in -> {
					if(in < 2)
						return Stream.empty();
					else
						return intStream(in).skip(2).filter(factor -> in % factor == 0);
				}); //Function returns a Stream of all factors of i, and returns an empty stream if i is prime.

		//Result list contains the factors of 4(2), 6(2, 3), 8(2, 4) and 9(3) in a single stream.
		Assert.assertEquals(Arrays.asList(2, 2, 3, 2, 4, 3), result.collect(Collectors.toList()));
	}

	/**
	 * Filter
	 * From:     Stream<A>
	 * Takes:    Function of A -> boolean
	 * Produces: Stream<A>
	 * By:       Testing every element in the stream with the function, it will keep elements where the function returns true and drop those that return false.
	 */
	@Test
	public void filterTest(){
		Stream<Integer> result = intStream(10)
				.filter(i -> i % 2 == 0); //Keeps even numbers, rejects odd ones.

		Assert.assertEquals(Arrays.asList(0, 2, 4, 6, 8), result.collect(Collectors.toList()));
	}


	/**
	 * ForEach
	 * From:     Stream<A>
	 * Takes:    Function of A -> void
	 * Produces: void
	 * By:       Applies the given function to every value of A, just like a for each loop would.
	 */
	@Test
	public void forEachTest(){
		intStream(10).forEach(i -> System.out.println("Value is: " + i)); //forEach returns void. It only goes at the end.
	}






}
