package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.expressions.values.Value;

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
			throw new EvaluationRuntimeException(this);
	}

	protected abstract Value computeResult();

	protected abstract boolean allOperandsValued();

}
