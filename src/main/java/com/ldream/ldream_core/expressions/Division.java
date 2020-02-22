/**
 * 
 */
package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.FactorizableValue;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.Value;

/**
 * @author alessandro
 *
 */
public class Division extends AbstractBinaryExpression {
	
	public static final int BASE_CODE = 707;

	public Division(
			Expression operand1,
			Expression operand2,
			Value operandValue1,
			Value operandValue2) {

		super(operand1,operand2,operandValue1,operandValue2);
	}

	public Division(
			Expression operand1, 
			Expression operand2) {

		super(operand1,operand2);
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new Division(
				operand1.bindActualComponent(componentReference, actualComponent),
				operand2.bindActualComponent(componentReference, actualComponent),
				operandValue1,
				operandValue2);
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
