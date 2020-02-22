package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.predicates.AbstractPredicate;
import com.ldream.ldream_core.coordination.predicates.Predicate;

public abstract class AbstractConstantFormula extends AbstractPredicate implements Predicate {

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
