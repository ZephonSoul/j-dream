/**
 * 
 */
package com.ldream.ldream_core.expressions;

import java.util.Arrays;
import java.util.List;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

/**
 * @author alessandro
 *
 */
@SuppressWarnings("serial")
public class Product extends AbstractEnnaryExpression {
	
	public Product(Expression[] operands,Number[] operandsValue) {
		super(operands,operandsValue);
	}
	
	public Product(Expression... operands) {
		super(operands);
	}
	
	public Product(List<Expression> operands) {
		super(operands);
	}

	@Override
	public Number getNeutralValue() {
		return 1;
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() * n2.doubleValue();
	}

	@Override
	public String getOperatorSymbol() {
		return "*";
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Product(Arrays.stream(operands)
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.toArray(Expression[]::new),
				operandsValue);
	}
}
