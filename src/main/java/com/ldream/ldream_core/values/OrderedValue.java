package com.ldream.ldream_core.values;

public interface OrderedValue extends Value {

	public boolean greaterThan(OrderedValue value);
	
	public boolean lessThan(OrderedValue value);
	
}
