package com.ldream.ldream_core.coordination.operations;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;

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
	public Operation instantiateComponentVariable(ComponentVariable componentVariable, Component actualComponent) {
		return new OperationsSequence(
				operations.stream()
				.map(o -> o.instantiateComponentVariable(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

}
