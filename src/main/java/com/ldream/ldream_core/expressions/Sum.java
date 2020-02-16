package com.ldream.ldream_core.expressions;

import java.util.Arrays;
import java.util.List;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

@SuppressWarnings("serial")
public class Sum extends AbstractEnnaryExpression {

	public Sum(Expression[] operands, Number[] operandsValue) {
		super(operands,operandsValue);
	}

	public Sum(Expression... operands) {
		super(operands);
	}

	public Sum(List<Expression> operands) {
		super(operands);
	}

	@Override
	public Number getNeutralValue() {
		return 0;
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() + n2.doubleValue();
	}

	@Override
	public String getOperatorSymbol() {
		return "+";
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Sum(Arrays.stream(operands)
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.toArray(Expression[]::new),
				operandsValue);
	}

}
