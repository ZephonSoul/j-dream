package com.ldream.ldream_core.coordination.predicates;

import java.util.Arrays;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.SetValue;
import com.ldream.ldream_core.values.Value;

public class Subset extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 51;
	
	public Subset(Expression... terms) {
		super(terms);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Subset)
				&& equalTerms((Subset) formula);
	}

	@Override
	public Formula bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return new Subset(
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
		return ((SetValue) v1).isSubsetOf((SetValue) v2);
	}

	@Override
	protected String getPredicateSymbol() {
		return "«";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
