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
public class Difference extends AbstractBinaryExpression {
	
	public Difference(
			Expression operand1,
			Expression operand2,
			Number operandValue1,
			Number operandValue2) {
		
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
				&& operand1.equals(((Difference) ex).getOperand1()) 
				&& operand2.equals(((Difference) ex).getOperand2());
	}

	@Override
	public String getOperatorSymbol() {
		return "-";
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() - n2.doubleValue();
	}

}
