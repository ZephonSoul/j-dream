package com.dream.core.expressions.values;

public interface OrderedValue extends Value {

	public boolean greaterThan(OrderedValue value);
	
	public boolean lessThan(OrderedValue value);
	
}
