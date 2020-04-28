package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.SetValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class SetRemove extends AbstractBinaryExpression {
	
	public static final int BASE_CODE = 313;
	
	public SetRemove(
			Expression operand1,
			Expression operand2,
			Value operandValue1,
			Value operandValue2) {

		super(operand1,operand2,operandValue1,operandValue2);
	}
	
	public SetRemove(Expression operand1, Expression operand2) {
		super(operand1,operand2);
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference,
			Instance<I> actual) {
		
		return new SetRemove(
				operand1.bindInstance(reference, actual),
				operand2.bindInstance(reference, actual),
				operandValue1,
				operandValue2);
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof SetRemove)
				&& equalOperands((SetRemove) ex);
	}

	@Override
	public String getOperatorSymbol() {
		return "(-)";
	}

	@Override
	public Value op(Value v1, Value v2) {
		if (!(v2 instanceof SetValue))
			throw new IncompatibleValueException(v2, SetValue.class);
		return ((SetValue) v2).removeValue(v1);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
