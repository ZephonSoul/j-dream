/**
 * 
 */
package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

/**
 * @author alessandro
 *
 */
@SuppressWarnings("serial")
public class Division extends AbstractBinaryExpression {

	public Division(
			Expression operand1,
			Expression operand2,
			Number operandValue1,
			Number operandValue2) {

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
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() / n2.doubleValue();
	}

}
