package com.ldream.ldream_core.coordination.operations;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class OperationsSequence extends AbstractOperation implements Operation {

	public OperationsSequence(Operation... operations) {
		super(operations);
	}
	
	public OperationsSequence(List<Operation> operations) {
		super(operations);
	}
	
	public String toString() {
		return "(" + String.join(";", getOperationsString()) + ")";
	}

	@Override
	public Operation bindActualComponent(ComponentInstance componentInstance, Component actualComponent) {
		return new OperationsSequence(
				operations.stream()
				.map(o -> o.bindActualComponent(componentInstance, actualComponent))
				.collect(Collectors.toList()));
	}

}
