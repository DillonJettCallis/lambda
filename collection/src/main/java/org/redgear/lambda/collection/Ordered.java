package org.redgear.lambda.collection;

import java.util.Comparator;

/**
 * Created by LordBlackHole on 7/17/2016.
 */
public interface Ordered<T> {

	Ordered<T> sort();

	Ordered<T> sort(Comparator<T> comparator);

	Ordered<T> reverse();

}
