/**
 * 
 */
package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.AdditiveValue;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.Value;

/**
 * @author alessandro
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
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Difference(
				operand1.bindActualComponent(componentVariable, actualComponent),
				operand2.bindActualComponent(componentVariable, actualComponent),
				operandValue1,
				operandValue2);
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
