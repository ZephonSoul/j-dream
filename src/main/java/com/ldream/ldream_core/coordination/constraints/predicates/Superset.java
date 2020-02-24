package com.ldream.ldream_core.coordination.constraints.predicates;

import java.util.Arrays;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.expressions.values.IncompatibleValueException;
import com.ldream.ldream_core.expressions.values.SetValue;
import com.ldream.ldream_core.expressions.values.Value;

public class Superset extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 151;
	
	public Superset(Expression... terms) {
		super(terms);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Superset)
				&& equalTerms((Superset) formula);
	}

	@Override
	public Formula bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return new Superset(
				Arrays.stream(terms)
				.map(t -> t.bindActualComponent(componentReference, actualComponent))
				.toArray(Expression[]::new));
	}

	@Override
	protected boolean testValues(Value v1, Value v2) {
		if (!(v1 instanceof SetValue))
			throw new IncompatibleValueException(v1, SetValue.class);
		if (!(v2 instanceof SetValue))
			throw new IncompatibleValueException(v2, SetValue.class);
		return ((SetValue) v1).isSupersetOf((SetValue) v2);
	}

	@Override
	protected String getPredicateSymbol() {
		return "»";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
