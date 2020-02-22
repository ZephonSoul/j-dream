package com.ldream.ldream_core.coordination.predicates;

import com.ldream.ldream_core.coordination.constraints.Formula;

public interface Predicate extends Formula {
		
	public boolean sat();
	
}
