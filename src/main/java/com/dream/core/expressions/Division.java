/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.FactorizableValue;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class Division extends AbstractBinaryExpression {
	
	public static final int BASE_CODE = 707;

	public Division(
			Expression operand1, 
			Expression operand2) {

		super(operand1,operand2);
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new Division(
				operand1.bindInstance(reference, actual),
				operand2.bindInstance(reference, actual));
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof Division) 
				&& operand1.equals(((Division) ex).getOperand1()) 
				&& operand2.equals(((Division) ex).getOperand2());
	}

	@Override
	public String getOperatorSymbol() {
		return "/";
	}

	@Override
	public Value op(Value v1, Value v2) {
		if (!(v1 instanceof FactorizableValue))
			throw new IncompatibleValueException(v1,FactorizableValue.class);
		if (!(v2 instanceof FactorizableValue))
			throw new IncompatibleValueException(v2,FactorizableValue.class);

		return ((FactorizableValue) v1).divideBy((FactorizableValue) v2);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
