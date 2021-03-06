package com.dream.core.coordination.constraints.predicates;

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
public class InSet extends AbstractBinaryPredicate implements Predicate {
	
	public final static int BASE_CODE = 150;
	
	/**
	 * @param term1 element to test set inclusion
	 * @param term2 set against which set inclusion is tested
	 */
	public InSet(Expression term1, Expression term2) {
		super(term1,term2);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof InSet)
				&& term1.equals(((InSet) formula).getTerm1())
				&& term2.equals(((InSet) formula).getTerm2());
	}

	@Override
	public <I> Predicate bindInstance(Instance<I> reference, Instance<I> actual) {
		return new InSet(
				term1.bindInstance(reference, actual),
				term2.bindInstance(reference, actual));
	}

	@Override
	protected boolean testValues(Value v1, Value v2) {
		if (!(v2 instanceof SetValue))
			throw new IncompatibleValueException(v2, SetValue.class);
		return ((SetValue) v2).contains(v1);
	}

	@Override
	protected String getPredicateSymbol() {
		return "ϵ";
	}

	@Override
	protected boolean isCommutative() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
