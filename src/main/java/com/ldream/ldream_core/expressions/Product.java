/**
 * 
 */
package com.ldream.ldream_core.expressions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

/**
 * @author alessandro
 *
 */
@SuppressWarnings("serial")
public class Product extends AbstractExpression {
	
	public Product(Expression... params) {
		super(params);
	}
	
	public Product(List<Expression> params) {
		super(params);
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
			ReferencedComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Product(params.stream()
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}
}
