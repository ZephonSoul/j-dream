package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.Formula;

public abstract class AbstractConstantFormula extends AbstractPredicate implements Predicate {

	@Override
	public Formula bindEntityReference(EntityInstanceReference componentVariable, EntityInstanceActual actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
