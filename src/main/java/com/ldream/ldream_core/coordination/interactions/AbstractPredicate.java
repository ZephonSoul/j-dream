package com.ldream.ldream_core.coordination.interactions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.Value;

public abstract class AbstractPredicate implements Predicate {

	List<Expression> terms;
	
	public AbstractPredicate(Expression... terms) {
		this.terms = Arrays.asList(terms);
	}
	
	public AbstractPredicate(List<Expression> terms) {
		this.terms = terms;
	}
	
	public List<Expression> getTerms() {
		return terms;
	}
	
	public String toString() {
		return terms.stream().map(Expression::toString).collect(Collectors.joining(getPredicateSymbol()));
	}	
	
	public boolean equalTerms(AbstractPredicate predicate) {
		return terms.equals(predicate.getTerms());
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Predicate)	&& equals((Predicate) o);
	}
	
	@Override
	public void clearCache() {
		terms.stream().forEach(Expression::clearCache);
	}
	
	@Override
	public boolean sat(Interaction i) {
		boolean skip = true;
		Value term_value = terms.get(0).eval(),
				lesser_term;
		for (Expression term : terms) {
			if (skip)
				skip = false;
			else {
				lesser_term = term.eval();
				if (!testValues(term_value,lesser_term))
					return false;
				else
					term_value = lesser_term;
			}
		}
		return true;
	};
	
	protected abstract boolean testValues(Value v1, Value v2);
	
	protected abstract String getPredicateSymbol();

}
