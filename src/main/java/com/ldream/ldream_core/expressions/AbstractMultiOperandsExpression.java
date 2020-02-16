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
public abstract class AbstractMultiOperandsExpression extends AbstractExpression implements Expression {

	protected Expression[] operands;
	protected Number[] operandsValue;

	public AbstractMultiOperandsExpression(Expression... operands) {
		this.operands = operands;
		this.operandsValue = new Number[operands.length];
	}

	public AbstractMultiOperandsExpression(List<Expression> operands) {
		this.operands = operands.toArray(Expression[]::new);
		this.operandsValue = new Number[operands.size()];
	}

	public Expression[] getOperands() {
		return operands;
	}

	@Override
	public int hashCode() {
		int operandsHashCode = 0;
		for (Expression operand : operands)
			operandsHashCode += operand.hashCode();
		return this.getClass().hashCode() + operandsHashCode; 
	}

	public boolean equals(Expression ex) {
		if (this.getClass().equals(ex.getClass()))
			return Arrays.equals(
					operands,
					((AbstractMultiOperandsExpression)ex).getOperands());
		else
			return false;
	}
	
	@Override
	public void evaluateOperands() {
		for (int i=0; i<operands.length; i++) {
			if (operandsValue[i] == null)
				operandsValue[i] = operands[i].eval();
		}
	}
	
	private boolean allOperandsValued() {
		for (Number operandValue : operandsValue)
			if (operandValue == null)
				return false;
		return true;
	}

	@Override
	public Number eval() {
		if (allOperandsValued()) {
			// check if all parameters are integer
			boolean int_operands = true;
			for (Expression n : operands) {
				if (n.eval().intValue() != (int)n.eval().doubleValue()) {
					int_operands = false;
					break;
				}
			}
			Number result = getNeutralValue();
			for (Expression n : operands)
				result = op(result,n.eval());
			// return integer result if all params are integer AND the result is integer
			if (int_operands && (result.intValue() == (int)result.doubleValue()))
				return result.intValue();
			else
				return result;
		} else
			//TODO: raise exception
			return null;
	}

	public String toString() {
		return Arrays.asList(operands).stream().map(Expression::toString).collect(Collectors.joining(getOperatorSymbol()));
	}

	public abstract String getOperatorSymbol();

	public abstract Number getNeutralValue();

	public abstract Number op(Number n1,Number n2);

}
