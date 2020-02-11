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
public class Difference extends AbstractExpression {

	public Difference(Expression... params) {
		super(params);
	}
	
	public Difference(List<Expression> params) {
		super(params);
	}

	@Override
	public Number getNeutralValue() {
		return 0;
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() - n2.doubleValue();
	}

	@Override
	public String getOperatorSymbol() {
		return "-";
	}

	@Override
	public Expression bindActualComponent(
			ReferencedComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {
		
		return new Difference(params.stream()
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

}
