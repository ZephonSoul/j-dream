/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.AdditiveValue;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class Difference extends AbstractBinaryExpression {
	
	public static final int BASE_CODE = 101;

	public Difference(
			Expression operand1,
			Expression operand2,
			Value operandValue1,
			Value operandValue2) {

		super(operand1,operand2,operandValue1,operandValue2);
	}

	public Difference(
			Expression operand1,
			Expression operand2) {

		super(operand1,operand2);
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new Difference(
				operand1.bindInstance(reference, actual),
				operand2.bindInstance(reference, actual));
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof Difference)
				&& equalOperands((Difference) ex);
	}

	@Override
	public String getOperatorSymbol() {
		return "-";
	}

	@Override
	public Value op(Value v1, Value v2) {
		if (!(v1 instanceof AdditiveValue))
			throw new IncompatibleValueException(v1,AdditiveValue.class);
		if (!(v2 instanceof AdditiveValue))
			throw new IncompatibleValueException(v2,AdditiveValue.class);
		
		return ((AdditiveValue) v1).subtract((AdditiveValue) v2);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
