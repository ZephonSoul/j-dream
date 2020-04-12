package com.ldream.ldream_core.expressions.values;

public interface AdditiveValue extends Value {

	public Value add(AdditiveValue value);
	
	public Value subtract(AdditiveValue value);
	
}
