package com.dream.core.expressions.values;

/**
 * @author Alessandro Maggi
 *
 */
public interface AdditiveValue extends Value {

	public Value add(AdditiveValue value);
	
	public Value subtract(AdditiveValue value);
	
}
