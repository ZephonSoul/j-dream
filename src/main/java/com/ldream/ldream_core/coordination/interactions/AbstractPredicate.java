package com.ldream.ldream_core.coordination.interactions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.expressions.Expression;

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
	public abstract boolean sat(Interaction i);
	
	protected abstract String getPredicateSymbol();

}
