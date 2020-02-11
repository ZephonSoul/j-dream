package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;

public interface Operation {

	public void evaluateParams();
	
	public void execute();
	
	public boolean equals(Operation op);

	public Operation instantiateComponentVariable(
			ComponentVariable componentVariable, 
			Component actualComponent);
	
}
