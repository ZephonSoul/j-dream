package com.ldream.ldream_core.values;

public interface FactorizableValue extends Value {

	public Value multiplyBy(FactorizableValue value);
	
	public Value divideBy(FactorizableValue value);
	
}
