package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractPredicate implements Predicate {
	
	@Override
	public boolean sat(Interaction i) {
		return sat();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Predicate)	&& equals((Predicate) o);
	}
	
	@Override
	public void evaluateExpressions() {}

}
