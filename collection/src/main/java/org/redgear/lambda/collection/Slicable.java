package org.redgear.lambda.collection;

import java.util.function.Function;

/**
 * Created by LordBlackHole on 7/15/2016.
 */
public interface Slicable<T> {


	Slicable<T> take(int num);

	Slicable<T> takeWhile(Function<? super T, ? extends Boolean> func);

	Slicable<T> drop(int num);

	Slicable<T> dropWhile(Function<? super T, ? extends Boolean> func);

	Slicable<T> slice(int start, int take);




}
