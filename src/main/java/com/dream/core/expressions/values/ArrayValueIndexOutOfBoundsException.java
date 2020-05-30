package com.dream.core.expressions.values;

/**
 * @author Alessandro Maggi
 *
 */
public class ArrayValueIndexOutOfBoundsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public <T> ArrayValueIndexOutOfBoundsException(ArrayValue value,int index) {
		super(getMessage(value,index));
	}
	
	private static <T> String getMessage(ArrayValue v, int index) {
		return String.format(
				"Index %d out of bounds for ArrayValue %s", 
				index,
				v.toString());
	}
	
}
