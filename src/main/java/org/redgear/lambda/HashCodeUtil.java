package org.redgear.lambda;


import java.util.List;

public final class HashCodeUtil {

	private final static int seed = 7;
	private final static int prime = 31;

	private static int start(int seed) {
		return prime * seed;
	}


	public static int hash(int seed, boolean value) {
		return start(seed) + (value ? 1 : 0);
	}

	public static int hash(int seed, char value) {
		return hash((int) value);
	}

	public static int hash(int seed, int value) {
		return start(seed) + value;
	}

	public static int hash(int seed, float value) {
		return hash(seed, Float.floatToIntBits(value));
	}

	public static int hash(int seed, long value) {
		return start(seed) + (int) (value ^ value >>> 32);
	}

	public static int hash(int seed, double value) {
		return hash(seed, Double.doubleToLongBits(value));
	}

	public static int hash(int seed, Object obj) {
		if (obj == null)
			return hash(seed, 0);
		else if (obj instanceof Object[]) {
			int ans = seed;

			for (Object val : (Object[]) obj)
				if (val != obj) //prevents infinite recursion of an array inside itself.
					ans = hash(ans, val);
			return ans;
		}
		else
			return hash(seed, obj.hashCode());

	}

	public static int hash(Object... objects) {
		int ans = seed;

		for (Object obj : objects)
			ans = hash(ans, obj);

		return ans;
	}

	public static int hash(Iterable<Object> objects) {
		int ans = seed;

		for (Object obj : objects)
			ans = hash(ans, obj);

		return ans;
	}
} 