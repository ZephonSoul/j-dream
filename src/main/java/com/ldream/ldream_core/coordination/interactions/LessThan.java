package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.expressions.Expression;

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
	public boolean sat(Interaction i) {
		boolean sat = true,
				skip = true;
		double 	term_value = terms.get(0).eval().doubleValue(),
				greater_term;
		for (Expression term : terms) {
			if (skip)
				skip = false;
			else {
				greater_term = term.eval().doubleValue();
				sat = term_value < greater_term;
				if (!sat)
					break;
				else
					term_value = greater_term;
			}
		}
		return sat;
	}

	@Override
	protected String getPredicateSymbol() {
		return "<";
	}

	@Override
	public boolean equals(Formula formula) {
		if (formula instanceof LessThan)
			return equalTerms((LessThan) formula);
		else
			return false;
	}

}
