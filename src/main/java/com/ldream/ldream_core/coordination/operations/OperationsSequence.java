package com.ldream.ldream_core.coordination.operations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class OperationsSequence extends AbstractOperation implements Operation {
	
	List<Operation> operations;

	public OperationsSequence(Operation... operations) {
		this.operations = Arrays.asList(operations);
	}
	
	public OperationsSequence(List<Operation> operations) {
		this.operations = operations;
	}
	
	public List<Operation> getOperations() {
		return operations;
	}
	
	protected List<String> getOperationsString() {
		return operations.stream().map(Operation::toString).collect(Collectors.toList());
	}
	
	public String toString() {
		return "(" + String.join(";", getOperationsString()) + ")";
	}
	
	@Override
	public void evaluateParams() {
		operations.stream().forEach(Operation::evaluateParams);
	}

	@Override
	public Operation bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return new OperationsSequence(
				operations.stream()
				.map(o -> o.bindActualComponent(componentReference, actualComponent))
				.collect(Collectors.toList()));
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		for (Operation o : operations)
			code += o.hashCode();
		return code;
	}
	
	public boolean equals(Operation op) {
		if (op instanceof OperationsSequence) {
			List<Operation> opList = ((OperationsSequence)op).getOperations();
			if (operations.size() == opList.size()) {
				for (int i=0; i<operations.size(); i++) {
					if (!operations.get(i).equals(opList.get(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute() {
		operations.stream().forEach(Operation::execute);
	}

}
