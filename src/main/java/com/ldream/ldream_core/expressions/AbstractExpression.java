package com.ldream.ldream_core.expressions;

@SuppressWarnings("serial")
public abstract class AbstractExpression extends Number implements Expression {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Expression)
				&& equals((Expression) o);
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

	@Override
	public Number eval() {
		evaluateOperands();
		if (allOperandsValued()) {
			return computeResult();
		}
		else
			//TODO: throw exception
			return null;
	}

	protected abstract Number computeResult();

	protected abstract boolean allOperandsValued();

}
