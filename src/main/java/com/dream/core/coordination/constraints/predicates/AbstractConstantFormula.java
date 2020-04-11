package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.constraints.Formula;

public abstract class AbstractConstantFormula extends AbstractPredicate implements Predicate {

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
