package com.ldream.ldream_core.coordination.operations;

public abstract class AbstractOperation implements Operation {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Operation)
				&& this.equals((Operation) o);
	}

}
