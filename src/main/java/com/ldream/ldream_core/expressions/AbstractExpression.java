package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.values.Value;

public abstract class AbstractExpression implements Expression {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Expression)
				&& equals((Expression) o);
	}

	@Override
	public Value eval() {
		evaluateOperands();
		if (allOperandsValued()) {
			return computeResult();
		}
		else
			//TODO: throw exception
			return null;
	}

	protected abstract Value computeResult();

	protected abstract boolean allOperandsValued();

}
