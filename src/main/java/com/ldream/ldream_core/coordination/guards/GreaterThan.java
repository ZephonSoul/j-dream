package com.ldream.ldream_core.coordination.guards;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.OrderedValue;
import com.ldream.ldream_core.values.Value;

public class GreaterThan extends AbstractEnnaryPredicate implements Predicate {

	public GreaterThan(Expression... terms) {
		super(terms);
	}

	public GreaterThan(List<Expression> terms) {
		super(terms);
	}

	@Override
	public Predicate bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new GreaterThan(terms.stream()
				.map(t -> t.bindActualComponent(componentReference,actualComponent))
				.collect(Collectors.toList()));
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

}
