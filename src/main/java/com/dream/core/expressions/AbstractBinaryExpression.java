package com.dream.core.expressions;

import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractBinaryExpression extends AbstractExpression implements Expression {

	protected Expression operand1,operand2;
	protected Value operandValue1,operandValue2;



	public AbstractBinaryExpression(Expression operand1, Expression operand2) {
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

	public boolean equalOperands(AbstractBinaryExpression ex) {
		return operand1.equals(ex.getOperand1())
				&& operand2.equals(ex.getOperand2());
	}

	@Override
	public void evaluateOperands() {
		if (operandValue1 == null)
			operandValue1 = operand1.eval();
		if (operandValue2 == null)
			operandValue2 = operand2.eval();
	}

	@Override
	public boolean allOperandsValued() {
		return operandValue1 != null
				&& operandValue2 != null;
	}

	@Override
	public Value computeResult() {
		Value result = op(operandValue1,operandValue2);
		return result;
	}

	@Override
	public void clearCache() {
		operand1.clearCache();
		operand2.clearCache();
		operandValue1 = null;
		operandValue2 = null;
	}

	public String toString() {
		return String.format("%s %s %s", 
				operand1.toString(),
				getOperatorSymbol(),
				operand2.toString()
				);
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode() + 
				operand1.hashCode() + operand2.hashCode();
	}

	public abstract String getOperatorSymbol();

	public abstract Value op(Value v1,Value v2);

}
