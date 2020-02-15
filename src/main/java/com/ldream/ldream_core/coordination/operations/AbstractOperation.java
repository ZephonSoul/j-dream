package com.ldream.ldream_core.coordination.operations;

public abstract class AbstractOperation implements Operation {

	@Override
	public boolean equals(Object o) {
		if (o instanceof Operation)
			return this.equals((Operation) o);
		else
			return false;
	}

}
