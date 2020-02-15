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

	protected List<Expression> operands;

	public AbstractMultiOperandsExpression(Expression... operands) {
		this.operands = Arrays.asList(operands);
	}
	
	public AbstractMultiOperandsExpression(List<Expression> operands) {
		this.operands = operands;
	}
	
	public List<Expression> getParams() {
		return operands;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() + operands.stream().mapToInt(Expression::hashCode).sum(); 
	}
	
	public boolean equals(Expression ex) {
		if (this.getClass().equals(ex.getClass())) {
			List<Expression> exOperands = ((AbstractMultiOperandsExpression)ex).getParams();
			if (operands.size() == exOperands.size()) {
				for (int i=0; i<operands.size(); i++) {
					if (!operands.get(i).equals(exOperands.get(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public Number eval() {
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
	}
	
	public String toString() {
		return operands.stream().map(Expression::toString).collect(Collectors.joining(getOperatorSymbol()));
	}
	
	public abstract String getOperatorSymbol();
	
	public abstract Number getNeutralValue();
	
	public abstract Number op(Number n1,Number n2);
	
}
