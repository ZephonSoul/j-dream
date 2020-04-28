package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
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
	
	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		return this;
	}

	protected abstract Value computeResult();

	protected abstract boolean allOperandsValued();

}
