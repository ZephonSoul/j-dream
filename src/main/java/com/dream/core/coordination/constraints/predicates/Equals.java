package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class Equals extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 0;

	public Equals(Expression... terms) {
		super(terms);
	}

	@Override
	protected String getPredicateSymbol() {
		return "=";
	}

	@Override
	public <I> Predicate bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new Equals(Arrays.stream(terms)
				.map(t -> t.bindInstance(reference,actual))
				.toArray(Expression[]::new));
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Equals)
				&& equalTerms((Equals) formula);
	}

	@Override
	protected boolean testValues(Value v1, Value v2) {
		return v1.equals(v2);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
