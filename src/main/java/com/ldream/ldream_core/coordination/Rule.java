package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public interface Rule {
	
	public boolean sat(Interaction i);
	
	public OperationsSet getOperationsForInteraction(Interaction i);

	public Rule instantiateComponentVariable(ComponentVariable componentVariable,Component actualComponent);
	
	public Rule getPILRule();
	
}
