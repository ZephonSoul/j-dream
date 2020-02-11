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
public abstract class AbstractExpression extends Number implements Expression {

	protected List<Expression> params;

	public AbstractExpression(Expression... params) {
		this.params = Arrays.asList(params);
	}
	
	public AbstractExpression(List<Expression> params) {
		this.params = params;
	}
	
	@Override
	public int intValue() {
		return eval().intValue();
	}

	@Override
	public long longValue() {
		return eval().longValue();
	}

	@Override
	public float floatValue() {
		return eval().floatValue();
	}

	@Override
	public double doubleValue() {
		return eval().doubleValue();
	}
	
	public List<Expression> getParams() {
		return params;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() + params.stream().mapToInt(Expression::hashCode).sum(); 
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Expression)
			return equals((Expression) o);
		else
			return false;
	}
	
	public boolean equals(Expression ex) {
		if (this.getClass().equals(ex.getClass())) {
			List<Expression> exParams = ((AbstractExpression)ex).getParams();
			if (params.size() == exParams.size()) {
				for (int i=0; i<params.size(); i++) {
					if (!params.get(i).equals(exParams.get(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public Number eval() {
		// check if all parameters are integer
		boolean int_params = true;
		for (Expression n : params) {
			if (n.eval().intValue() != (int)n.eval().doubleValue()) {
				int_params = false;
				break;
			}
		}
		Number result = getNeutralValue();
		for (Expression n : params)
			result = op(result,n.eval());
		// return integer result if all params are integer AND the result is integer
		if (int_params && (result.intValue() == (int)result.doubleValue()))
			return result.intValue();
		else
			return result;
	}
	
	public String toString() {
		return params.stream().map(Expression::toString).collect(Collectors.joining(getOperatorSymbol()));
	}
	
	public abstract String getOperatorSymbol();
	
	public abstract Number getNeutralValue();
	
	public abstract Number op(Number n1,Number n2);
	
}
