package com.ldream.ldream_core.expressions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;

@SuppressWarnings("serial")
public class Sum extends AbstractExpression {

	public Sum(Expression... params) {
		super(params);
	}
	
	public Sum(List<Expression> params) {
		super(params);
	}

	@Override
	public Number getNeutralValue() {
		return 0;
	}

	@Override
	public Number op(Number n1, Number n2) {
		return n1.doubleValue() + n2.doubleValue();
	}

	@Override
	public String getOperatorSymbol() {
		return "+";
	}

	@Override
	public Expression instantiateComponentVariable(
			ComponentVariable componentVariable, 
			Component actualComponent) {
		
		return new Sum(params.stream()
				.map(e -> e.instantiateComponentVariable(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

}
