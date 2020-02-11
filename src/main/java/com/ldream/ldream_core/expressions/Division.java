/**
 * 
 */
package com.ldream.ldream_core.expressions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;

/**
 * @author alessandro
 *
 */
@SuppressWarnings("serial")
public class Division extends AbstractExpression {
	
	public Division(Expression... params) {
		super(params);
	}
	
	public Division(List<Expression> params) {
		super(params);
	}

	@Override
	public Number getNeutralValue() {
		return 1;
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() / n2.doubleValue();
	}

	@Override
	public String getOperatorSymbol() {
		return "/";
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			Component actualComponent) {
		
		return new Division(params.stream()
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

}
