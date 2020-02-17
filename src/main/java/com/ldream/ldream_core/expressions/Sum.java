package com.ldream.ldream_core.expressions;

import java.util.Arrays;
import java.util.List;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.AdditiveValue;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.Value;

public class Sum extends AbstractEnnaryExpression {

	public Sum(Expression[] operands, Value[] operandsValue) {
		super(operands,operandsValue);
	}

	public Sum(Expression... operands) {
		super(operands);
	}

	public Sum(List<Expression> operands) {
		super(operands);
	}

	@Override
	public Value op(Value v1, Value v2) {
		if (!(v1 instanceof AdditiveValue))
			throw new IncompatibleValueException(v1,AdditiveValue.class);
		if (!(v2 instanceof AdditiveValue))
			throw new IncompatibleValueException(v2,AdditiveValue.class);
		
		return ((AdditiveValue) v1).add((AdditiveValue) v2);
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
