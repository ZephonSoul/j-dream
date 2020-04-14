package com.dream.core.expressions.values;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.expressions.Expression;

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

	@Override
	public Value eval() {
		return this;
	}

	@Override
	public void evaluateOperands() {}

	@Override
	public void clearCache() {}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof PrimitiveValue)
				&& equals((PrimitiveValue<?>) ex);
	}

	@Override
	public Expression bindEntityReference(EntityInstanceReference componentReference,
			EntityInstanceActual actualComponent) {
		return this;
	}

}
