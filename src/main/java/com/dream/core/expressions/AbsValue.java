/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class AbsValue extends AbstractExpression {

	private Expression operand;
	private NumberValue value;
	
	public AbsValue(Expression operand) {
		this.operand = operand;
	}
	
	public Expression getOperand() {
		return operand;
	}

	@Override
	public void evaluateOperands() {
		if (value==null) {
			NumberValue operandVal = (NumberValue) operand.eval();
			if (operandVal instanceof NumberValue)
				value = new NumberValue(Math.abs(operandVal.getRawValue().doubleValue()));
			else
				throw new IncompatibleValueException(operandVal, NumberValue.class);
		}
	}

	@Override
	public void clearCache() {
		value = null;
		operand.clearCache();
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof AbsValue) &&
				((AbsValue)ex).getOperand().equals(operand);
	}

	@Override
	protected Value computeResult() {
		return value;
	}

	@Override
	public boolean allOperandsValued() {
		return value != null;
	}
	
	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		return new AbsValue(operand.bindInstance(reference, actual));
	}
	
	public String toString() {
		if (value != null)
			return value.toString();
		else
			return String.format("|%s|", operand.toString());
	}

}
