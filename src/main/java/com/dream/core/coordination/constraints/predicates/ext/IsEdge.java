package com.dream.core.coordination.constraints.predicates.ext;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.coordination.constraints.predicates.AbstractPredicate;
import com.dream.core.coordination.constraints.predicates.Predicate;

public class IsEdge extends AbstractPredicate implements Predicate {

	@Override
	public boolean equals(Formula formula) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sat() {
		// TODO Auto-generated method stub
		return false;
	}

}
