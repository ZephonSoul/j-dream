package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.constraints.Formula;

/**
 * @author Alessandro Maggi
 *
 */
public interface Predicate extends Formula {
		
	public boolean sat();
	
}
