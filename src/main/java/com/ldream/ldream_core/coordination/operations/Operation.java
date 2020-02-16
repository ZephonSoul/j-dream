package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.Bindable;

public interface Operation extends Bindable<Operation> {

	public void evaluateOperands();
	
	public void execute();
	
	public boolean equals(Operation op);

	public void clearCache();
	
}
