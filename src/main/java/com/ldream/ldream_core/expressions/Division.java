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
public class Division extends AbstractExpression {

	private Expression operand1,operand2;

	public Division(Expression operand1, Expression operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	/**
	 * @return the operand1
	 */
	public Expression getOperand1() {
		return operand1;
	}

	/**
	 * @return the operand2
	 */
	public Expression getOperand2() {
		return operand2;
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new Division(
				operand1.bindActualComponent(componentReference, actualComponent),
				operand2.bindActualComponent(componentReference, actualComponent));
	}

	@Override
	public Number eval() {
		// check if both operands are integer
		Number val1 = operand1.eval(),
				val2 = operand2.eval();
		boolean int_operands = 
				(val1.intValue() == (int)val1.doubleValue()) &&
				(val2.intValue() == (int)val2.doubleValue());
		Number result = val1.doubleValue() / val2.doubleValue();
		// return integer result if both operands are integer AND the result is integer
		if (int_operands && (result.intValue() == (int)result.doubleValue()))
			return result.intValue();
		else
			return result;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof Division) &&
				operand1.equals(((Division) ex).getOperand1()) &&
				operand2.equals(((Division) ex).getOperand2());
	}

	public String toString() {
		return operand1.toString() + "/" + operand2.toString();
	}
	
}
