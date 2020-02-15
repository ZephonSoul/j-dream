package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class Skip implements Operation {
	
	public static int BASE_CODE = 0;

	public Skip() {}

	@Override
	public void evaluateParams() {}

	@Override
	public void execute() {}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Skip);
	}
	
	public boolean equals(Operation op) {
		return (op instanceof Skip);
	}
	
	public String toString() {
		return "skip()";
	}

	@Override
	public Operation bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return this;
	}

}
