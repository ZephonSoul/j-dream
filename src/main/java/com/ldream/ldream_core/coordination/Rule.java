package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public interface Rule extends Bindable<Rule> {
	
	public boolean sat(Interaction i);
	
	public OperationsSet getOperationsForInteraction(Interaction i);
	
	public Rule expandDeclarations();
	
}
