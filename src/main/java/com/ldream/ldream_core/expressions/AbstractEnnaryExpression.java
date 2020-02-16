/**
 * 
 */
package com.ldream.ldream_core.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author alessandro
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractEnnaryExpression extends AbstractExpression implements Expression {

	protected Expression[] operands;
	protected Number[] operandsValue;

	public AbstractEnnaryExpression(Expression[] operands,
			Number[] operandsValue) {
		this.operands = operands;
		this.operandsValue = operandsValue;
	}

	public AbstractEnnaryExpression(Expression... operands) {
		this(operands,new Number[operands.length]);
	}

	public AbstractEnnaryExpression(List<Expression> operands) {
		this(operands.toArray(Expression[]::new));
	}

	public Expression[] getOperands() {
		return operands;
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode() + 
				Arrays.stream(operands).mapToInt(Expression::hashCode).sum(); 
	}

	public boolean equals(Expression ex) {
		return (this.getClass().equals(ex.getClass()))
				&& Arrays.equals(
						operands,
						((AbstractEnnaryExpression)ex).getOperands());
	}

	@Override
	public void evaluateOperands() {
		for (int i=0; i<operands.length; i++) {
			if (operandsValue[i] == null)
				operandsValue[i] = operands[i].eval();
		}
	}

	@Override
	public boolean allOperandsValued() {
		for (Number operandValue : operandsValue)
			if (operandValue == null)
				return false;
		return true;
	}

	@Override
	public Number computeResult() {
		// check if all parameters are integer
		boolean int_operands = true;
		for (Number n : operandsValue) {
			if (n.intValue() != (int)n.doubleValue()) {
				int_operands = false;
				break;
			}
		}
		Number result = getNeutralValue();
		for (Number n : operandsValue)
			result = op(result,n);
		// return integer result if all params are integer AND the result is integer
		if (int_operands && (result.intValue() == (int)result.doubleValue()))
			return result.intValue();
		else
			return result;
	}

	@Override
	public void clearCache() {
		operandsValue = new Number[operands.length];
	}

	public String toString() {
		return Arrays.stream(operands)
				.map(Expression::toString)
				.collect(Collectors.joining(getOperatorSymbol()));
	}

	public abstract String getOperatorSymbol();

	public abstract Number getNeutralValue();

	public abstract Number op(Number n1,Number n2);

}
