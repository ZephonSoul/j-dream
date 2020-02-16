package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class Skip extends AbstractOperation implements Operation {
	
	final static int BASE_CODE = 0;
	private static Skip instance;

	public static Skip getInstance() {
		if (instance == null)
			instance = new Skip();
		return instance;
	}

	@Override
	public void evaluateOperands() {}

	@Override
	public void execute() {}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}
	
	@Override
	public Operation bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}
	
	public boolean equals(Operation op) {
		return (op instanceof Skip);
	}
	
	public String toString() {
		return "skip()";
	}

}
