package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.OrderedValue;
import com.dream.core.expressions.values.Value;

public class GreaterThan extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 50;

	public GreaterThan(Expression... terms) {
		super(terms);
	}

	@Override
	public Predicate bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new GreaterThan(Arrays.stream(terms)
				.map(t -> t.bindActualComponent(componentReference,actualComponent))
				.toArray(Expression[]::new));
	}

	@Override
	protected String getPredicateSymbol() {
		return ">";
	}
	
	@Override
	protected boolean testValues(Value v1, Value v2) {
		if (!(v1 instanceof OrderedValue))
			throw new IncompatibleValueException(v1, OrderedValue.class);
		if (!(v2 instanceof OrderedValue))
			throw new IncompatibleValueException(v2, OrderedValue.class);
		return ((OrderedValue) v1).greaterThan((OrderedValue) v2);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof GreaterThan)
				&& equalTerms((GreaterThan) formula);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
