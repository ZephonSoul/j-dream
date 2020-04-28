package com.dream.core.expressions;

import java.util.Arrays;
import java.util.List;

import com.dream.core.Instance;
import com.dream.core.expressions.values.AdditiveValue;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class Sum extends AbstractEnnaryExpression {
	
	public static final int BASE_CODE = 231;

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
	public <I> Expression bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new Sum(Arrays.stream(operands)
				.map(e -> e.bindInstance(reference, actual))
				.toArray(Expression[]::new),
				operandsValue);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
