package com.ldream.ldream_core.values;

public class PrimitiveValue<T> extends AbstractValue implements Value {

	protected T rawValue;
	
	public PrimitiveValue(T rawValue) {
		this.rawValue = rawValue;
	}
	
	public T getRawValue() {
		return rawValue;
	}
	
	public boolean equals(PrimitiveValue<?> value) {
		return rawValue.equals(value.getRawValue());
	}
	
	@Override
	public boolean equals(Value value) {
		return (value instanceof PrimitiveValue)
				&& equals(((PrimitiveValue<?>) value));
	}
	
	@Override
	public int hashCode() {
		return rawValue.hashCode();
	}
	
	public String toString() {
		return rawValue.toString();
	}

}
