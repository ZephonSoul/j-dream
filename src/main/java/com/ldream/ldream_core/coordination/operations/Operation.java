package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.Bindable;

public interface Operation extends Bindable<Operation> {

	public void evaluateParams();
	
	public void execute();
	
	public boolean equals(Operation op);
	
}
