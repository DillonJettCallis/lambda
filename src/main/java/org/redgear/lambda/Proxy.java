package org.redgear.lambda;

import org.redgear.lambda.function.Func;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Created by dcallis on 11/4/2015.
 */
public class Proxy<T> implements InvocationHandler {

	private final Map<String, List<MethodValue>> methods = new HashMap<>();

	private final T proxiedObject;

	public Proxy(Class<T> proxyClass){
		@SuppressWarnings("unchecked")
		T obj = (T) newProxyInstance(proxyClass.getClassLoader(), new Class[]{proxyClass}, this);

		this.proxiedObject = obj;
	}

	public T getProxiedObject() {
		return proxiedObject;
	}

	public Map<String, List<MethodValue>> getMethods() {
		return methods;
	}

	public void addMethod(String name, Func<?, ?> func) {
		try {
			methods.computeIfAbsent(name, key -> new ArrayList<>()).add(new MethodValue(func));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Could not find the apply method!", e);
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class[] argTypes = swapPrimitivesWithWrappers(method.getParameterTypes());

		MethodValue pair = methods.get(method.getName())
				.stream()
				.filter(test ->
						argTypes.length == test.paramaterTypes.length &&
								IntStream.range(0, argTypes.length).allMatch(index -> argTypes[index].isAssignableFrom(test.paramaterTypes[index])))
				.findFirst()
				.orElseThrow(() -> new NoSuchMethodException("Method: " + method.getName() + ", does not exist on this proxy."));


		return pair.method.invoke(pair.func, args);
	}

	private Class[] swapPrimitivesWithWrappers(Class[] input){
		for(int i = 0; i < input.length; i++) {
			if (input[i] == int.class)
				input[i] = Integer.class;

			if (input[i] == long.class)
				input[i] = Long.class;

			if (input[i] == byte.class)
				input[i] = Byte.class;

			if (input[i] == short.class)
				input[i] = Short.class;

			if (input[i] == char.class)
				input[i] = Character.class;

			if (input[i] == boolean.class)
				input[i] = Boolean.class;

			if (input[i] == float.class)
				input[i] = Float.class;

			if (input[i] == double.class)
				input[i] = Double.class;
		}

		return input;
	}

	private static class MethodValue {

		private final Func<?, ?> func;
		private final Method method;
		private final Class[] paramaterTypes;

		public MethodValue(Func<?, ?> func) throws NoSuchMethodException {
			this.func = func;
//			λ.Type t = func.getType();
			paramaterTypes = null;
//			this.paramaterTypes = t.parameterTypes();

			Class<?>[] params =  new Class[func.arity()];
			Arrays.fill(params, Object.class);

			this.method = func.getClass().getMethod("apply", params);
		}
	}

}
