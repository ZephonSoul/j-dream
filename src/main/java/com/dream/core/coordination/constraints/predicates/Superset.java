package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.SetValue;
import com.dream.core.expressions.values.Value;

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
	public Formula bindEntityReference(EntityInstanceReference componentReference, EntityInstanceActual actualComponent) {
		return new Superset(
				Arrays.stream(terms)
				.map(t -> t.bindEntityReference(componentReference, actualComponent))
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
