package com.dream.core.coordination.constraints.predicates;

import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractBinaryPredicate extends AbstractPredicate implements Predicate {

	Expression term1;
	Expression term2;
	
	public AbstractBinaryPredicate(Expression term1, Expression term2) {
		this.term1 = term1;
		this.term2 = term2;
	}
	
	public Expression getTerm1() {
		return term1;
	}
	
	public Expression getTerm2() {
		return term2;
	}
	
	public boolean equalTerms(AbstractBinaryPredicate predicate) {
		return term1.equals(predicate.getTerm1()) 
				&& term2.equals(predicate.getTerm2())
				|| (isCommutative()
						&& term1.equals(predicate.getTerm2())
						&& term2.equals(predicate.getTerm1()));
	}
	
	@Override
	public void clearCache() {
		term1.clearCache();
		term2.clearCache();
	}
	
	@Override
	public boolean sat() {
		return testValues(term1.eval(),term2.eval());
	};
	
	public String toString() {
		return String.format("%s %s %s",
				term1.toString(),
				getPredicateSymbol(),
				term2.toString());
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() + 
				term1.hashCode() + term2.hashCode();
	}
	
	@Override
	public void evaluateExpressions() {
		term1.evaluateOperands();
		term2.evaluateOperands();
	}
	
	protected abstract boolean testValues(Value v1, Value v2);
	
	protected abstract String getPredicateSymbol();
	
	protected abstract boolean isCommutative();

}
