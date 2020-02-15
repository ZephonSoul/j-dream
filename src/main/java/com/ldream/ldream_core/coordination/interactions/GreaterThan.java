package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.expressions.Expression;

public class GreaterThan extends AbstractPredicate implements Predicate {

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
	public boolean sat(Interaction i) {
		boolean sat = true,
				skip = true;
		double 	term_value = terms.get(0).eval().doubleValue(),
				lesser_term;
		for (Expression term : terms) {
			if (skip)
				skip = false;
			else {
				lesser_term = term.eval().doubleValue();
				sat = term_value > lesser_term;
				if (!sat)
					break;
				else
					term_value = lesser_term;
			}
		}
		return sat;
	}

	@Override
	protected String getPredicateSymbol() {
		return ">";
	}

}
