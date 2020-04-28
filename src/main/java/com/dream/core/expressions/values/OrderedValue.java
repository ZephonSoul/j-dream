package com.dream.core.expressions.values;

/**
 * @author Alessandro Maggi
 *
 */
public interface OrderedValue extends Value {

	public boolean greaterThan(OrderedValue value);
	
	public boolean lessThan(OrderedValue value);
	
}
