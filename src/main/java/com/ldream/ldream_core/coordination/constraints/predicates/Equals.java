package com.ldream.ldream_core.coordination.constraints.predicates;

import java.util.Arrays;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.expressions.values.Value;

public class Equals extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 0;

	public Equals(Expression... terms) {
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

		return new Equals(Arrays.stream(terms)
				.map(t -> t.bindActualComponent(componentReference,actualComponent))
				.toArray(Expression[]::new));
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
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
