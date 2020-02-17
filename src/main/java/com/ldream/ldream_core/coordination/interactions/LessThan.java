package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.OrderedValue;
import com.ldream.ldream_core.values.Value;

public class LessThan extends AbstractPredicate implements Predicate {

	public LessThan(Expression... terms) {
		super(terms);
	}

	public LessThan(List<Expression> terms) {
		super(terms);
	}

	@Override
	public Predicate bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new LessThan(terms.stream()
				.map(t -> t.bindActualComponent(componentReference,actualComponent))
				.collect(Collectors.toList()));
	}
	
	@Override
	public boolean testValues(Value v1, Value v2) {
		if (!(v1 instanceof OrderedValue))
			throw new IncompatibleValueException(v1, OrderedValue.class);
		if (!(v2 instanceof OrderedValue))
			throw new IncompatibleValueException(v2, OrderedValue.class);
		return ((OrderedValue) v1).lessThan((OrderedValue) v2);
	}

	@Override
	protected String getPredicateSymbol() {
		return "<";
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof LessThan)
				&& equalTerms((LessThan) formula);
	}

}
