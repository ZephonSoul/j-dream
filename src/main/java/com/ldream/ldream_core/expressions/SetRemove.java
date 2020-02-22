package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.SetValue;
import com.ldream.ldream_core.values.Value;

public class SetRemove extends AbstractBinaryExpression implements Expression {
	
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
	public Expression bindActualComponent(ComponentInstance componentReference,
			ActualComponentInstance actualComponent) {
		return new SetRemove(
				operand1.bindActualComponent(componentReference, actualComponent),
				operand2.bindActualComponent(componentReference, actualComponent),
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
