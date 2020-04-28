package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.SetValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class Subset extends AbstractEnnaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 51;
	
	public Subset(Expression... terms) {
		super(terms);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Subset)
				&& equalTerms((Subset) formula);
	}

	@Override
	public <I> Predicate bindInstance(Instance<I> reference, Instance<I> actual) {
		return new Subset(
				Arrays.stream(terms)
				.map(t -> t.bindInstance(reference, actual))
				.toArray(Expression[]::new));
	}

	@Override
	protected boolean testValues(Value v1, Value v2) {
		if (!(v1 instanceof SetValue))
			throw new IncompatibleValueException(v1, SetValue.class);
		if (!(v2 instanceof SetValue))
			throw new IncompatibleValueException(v2, SetValue.class);
		return ((SetValue) v1).isSubsetOf((SetValue) v2);
	}

	@Override
	protected String getPredicateSymbol() {
		return "«";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
