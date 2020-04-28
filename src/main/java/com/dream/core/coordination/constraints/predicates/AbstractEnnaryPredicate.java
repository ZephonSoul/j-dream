package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractEnnaryPredicate extends AbstractPredicate implements Predicate {

	Expression[] terms;
	
	public AbstractEnnaryPredicate(Expression... terms) {
		this.terms = terms;
	}
	
	public Expression[] getTerms() {
		return terms;
	}
	
	public String toString() {
		return Arrays.stream(terms).map(Expression::toString).collect(Collectors.joining(" " + getPredicateSymbol() + " "));
	}	
	
	public boolean equalTerms(AbstractEnnaryPredicate predicate) {
		return terms.equals(predicate.getTerms());
	}
	
	@Override
	public void clearCache() {
		Arrays.stream(terms).forEach(Expression::clearCache);
	}
	
	@Override
	public boolean sat() {
		Value term_value = terms[0].eval(),
				lesser_term;
		for (int j=1; j<terms.length; j++) {
			lesser_term = terms[j].eval();
			if (!testValues(term_value,lesser_term))
				return false;
			else
				term_value = lesser_term;
		}
		return true;
	};
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() +
				Arrays.stream(terms).mapToInt(Expression::hashCode).sum();
	}
	
	protected abstract boolean testValues(Value v1, Value v2);
	
	protected abstract String getPredicateSymbol();

}
