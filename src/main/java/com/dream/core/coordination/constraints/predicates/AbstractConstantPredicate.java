package com.dream.core.coordination.constraints.predicates;

import com.dream.core.Instance;

public abstract class AbstractConstantPredicate extends AbstractPredicate implements Predicate {

	@Override
	public <I> Predicate bindInstance(Instance<I> reference, Instance<I> actual) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
