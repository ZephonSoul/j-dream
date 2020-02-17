package com.ldream.ldream_core.values;

public class IncompatibleValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public <T> IncompatibleValueException(Value v, Class<T> expectedValueClass) {
		super(getMessage(v,expectedValueClass));
	}
	
	private static <T> String getMessage(Value v, Class<T> expectedValueClass) {
		return String.format(
				"Value %s not conforming to %s", 
				v.toString(),
				expectedValueClass.getSimpleName());
	}
	
}
