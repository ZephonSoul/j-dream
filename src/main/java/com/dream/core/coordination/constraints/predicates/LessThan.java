package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.OrderedValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class LessThan extends AbstractEnnaryPredicate implements Predicate {
		
	public final static int BASE_CODE = 100;

	public LessThan(Expression... terms) {
		super(terms);
	}

	@Override
	public <I> Predicate bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new LessThan(Arrays.stream(terms)
				.map(t -> t.bindInstance(reference,actual))
				.toArray(Expression[]::new));
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
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
