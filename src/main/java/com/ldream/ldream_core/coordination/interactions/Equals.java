package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;
import com.ldream.ldream_core.expressions.Expression;

public class Equals extends AbstractPredicate implements Predicate {
	
	public Equals(Expression... terms) {
		super(terms);
	}
	
	public Equals(List<Expression> terms) {
		super(terms);
	}

	@Override
	public boolean sat(Interaction i) {
		boolean sat = true;
		Number term_value = terms.get(0).eval();
		for (Expression term : terms) {
			sat = term_value.equals(term.eval());
			if (!sat)
				break;
		}
		return sat;
	}

	@Override
	protected String getPredicateSymbol() {
		return "=";
	}

	@Override
	public Predicate bindActualComponent(ReferencedComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return new Equals(terms.stream()
				.map(t -> t.bindActualComponent(componentVariable,actualComponent))
				.collect(Collectors.toList()));
	}

}
