package com.ldream.ldream_core.coordination.constraints.predicates;

import com.ldream.ldream_core.coordination.constraints.Formula;

public interface Predicate extends Formula {
		
	public boolean sat();
	
}
