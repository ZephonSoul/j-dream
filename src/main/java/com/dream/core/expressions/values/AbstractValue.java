package com.dream.core.expressions.values;

public abstract class AbstractValue implements Value {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Value);
	}

}
