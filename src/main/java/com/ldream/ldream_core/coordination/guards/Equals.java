package com.ldream.ldream_core.coordination.guards;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.Value;

public class Equals extends AbstractEnnaryPredicate implements Predicate {

	public Equals(Expression... terms) {
		super(terms);
	}

	public Equals(List<Expression> terms) {
		super(terms);
	}

	@Override
	protected String getPredicateSymbol() {
		return "=";
	}

	@Override
	public Predicate bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new Equals(terms.stream()
				.map(t -> t.bindActualComponent(componentReference,actualComponent))
				.collect(Collectors.toList()));
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Equals)
				&& equalTerms((Equals) formula);
	}

	@Override
	protected boolean testValues(Value v1, Value v2) {
		return v1.equals(v2);
	}

}
