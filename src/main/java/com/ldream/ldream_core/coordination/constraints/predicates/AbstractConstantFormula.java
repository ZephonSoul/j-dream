package com.ldream.ldream_core.coordination.constraints.predicates;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;

public abstract class AbstractConstantFormula extends AbstractPredicate implements Predicate {

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
