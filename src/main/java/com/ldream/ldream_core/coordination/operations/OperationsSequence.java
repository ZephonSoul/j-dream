package com.ldream.ldream_core.coordination.operations;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

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
	public Operation bindActualComponent(ReferencedComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return new OperationsSequence(
				operations.stream()
				.map(o -> o.bindActualComponent(componentReference, actualComponent))
				.collect(Collectors.toList()));
	}

}
