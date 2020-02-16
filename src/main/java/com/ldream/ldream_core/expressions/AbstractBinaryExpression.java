package com.ldream.ldream_core.expressions;

@SuppressWarnings("serial")
public abstract class AbstractBinaryExpression extends AbstractExpression implements Expression {

	protected Expression operand1,operand2;
	protected Number operandValue1,operandValue2;
	
	public AbstractBinaryExpression(
			Expression operand1,
			Expression operand2,
			Number operandValue1,
			Number operandValue2) {
		
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operandValue1 = operandValue1;
		this.operandValue2 = operandValue2;
	}

	public AbstractBinaryExpression(Expression operand1, Expression operand2) {
		this(operand1,operand2,null,null);
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
	public void evaluateOperands() {
		if (operandValue1 == null)
			operandValue1 = operand1.eval();
		if (operandValue2 == null)
			operandValue2 = operand1.eval();
	}

	@Override
	public boolean allOperandsValued() {
		return operandValue1 != null
				&& operandValue2 != null;
	}

	@Override
	public Number computeResult() {
		// check if all operands are integer
		boolean int_operands = 
				(operandValue1.intValue() == (int)operandValue1.doubleValue())
				&& (operandValue2.intValue() == (int)operandValue2.doubleValue());
		Number result = op(operandValue1,operandValue2);
		// return integer result if all operands are integer AND the result is integer
		if (int_operands && (result.intValue() == (int)result.doubleValue()))
			return result.intValue();
		else
			return result;
	}

	@Override
	public void clearCache() {
		operandValue1 = null;
		operandValue2 = null;
	}

	public String toString() {
		return operand1.toString() + getOperatorSymbol() + operand2.toString();
	}

	public abstract String getOperatorSymbol();

	public abstract Number op(Number n1,Number n2);

}
