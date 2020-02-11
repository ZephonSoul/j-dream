package com.ldream.ldream_core.output;

import com.ldream.ldream_core.coordination.operations.OperationsSet;

public class OperationsWritable implements Writable {
	
	private OperationsSet opsSet;

	public OperationsWritable(OperationsSet opsSet) {
		this.opsSet = opsSet;
	}

	@Override
	public String getString() {
		return "Operations performed = " + opsSet.toString();
	}

}
