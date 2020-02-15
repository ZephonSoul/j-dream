package com.ldream.ldream_core.expressions;

@SuppressWarnings("serial")
public abstract class AbstractExpression extends Number implements Expression {
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Expression)
			return equals((Expression) o);
		else
			return false;
	}

	@Override
	public int intValue() {
		return eval().intValue();
	}

	@Override
	public long longValue() {
		return eval().longValue();
	}

	@Override
	public float floatValue() {
		return eval().floatValue();
	}

	@Override
	public double doubleValue() {
		return eval().doubleValue();
	}
	
}
