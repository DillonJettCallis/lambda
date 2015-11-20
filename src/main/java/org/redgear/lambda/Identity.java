package org.redgear.lambda;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public interface Identity {


	List<Object> identity();

	default Optional<List<String>> labels(){
		return Optional.empty();
	}

	static IntSupplier hashcodeCalculator(Identity in){
		return () -> Identity.hashcode(in);
	}

	static int hashcode(Identity in){
		return HashCodeUtil.hash(in.identity());
	}

	static Predicate<Object> equalsCalculator(Identity first){
		return second -> equals(first, second);
	}

	static boolean equals(Identity first, Object second){
		//Neither can be null
		if(first == null || second == null)
			return false;

		//They are the same object
		if(first == second)
			return true;

		//Second must be a child or the same as first.
		if(!first.getClass().isAssignableFrom(second.getClass()))
			return false;

		List<Object> firstList = first.identity();
		List<Object> secondList = ((Identity) second).identity(); //We can cast because second isAssignableFrom first.

		int firstSize = firstList.size();

		if(firstSize == 0 || firstSize != secondList.size())
			return false;

		for(int i = 0; i < firstSize; i++)
			if(!Objects.deepEquals(firstList.get(i), secondList.get(i)))
				return false;

		return true;
	}

	static Supplier<String> toStringCalculator(Identity source){
		return () -> toString(source);
	}

	static String toString(Identity source){
		if(source == null)
			return "null";
		else {
			List<Object> id = source.identity();
			if(id == null)
				return "<empty>";
			else {
				List<String> labels = source.labels().orElse(null);

				if(labels != null && !labels.isEmpty()){
					int labelSize = labels.size();

					return IntStream.range(0, id.size()).mapToObj(i -> {
						if (labelSize >= i)
							return String.valueOf(labels.get(i)) + ": " + String.valueOf(id.get(i));
						else
							return String.valueOf(id.get(i));
					}).collect(Collectors.joining(", ", "[", "]"));

				}
				else
					return id.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
			}
		}
	}
}
