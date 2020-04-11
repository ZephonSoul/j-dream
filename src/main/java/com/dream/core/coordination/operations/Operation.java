package com.dream.core.coordination.operations;

import com.dream.core.Bindable;

public interface Operation extends Bindable<Operation> {

	public void evaluateOperands();
	
	public void execute();
	
	public boolean equals(Operation op);

	public void clearCache();
	
}
