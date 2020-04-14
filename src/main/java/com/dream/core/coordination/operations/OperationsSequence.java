package com.dream.core.coordination.operations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;

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
	
	@Override
	public void evaluateOperands() {
		operations.stream().forEach(Operation::evaluateOperands);
	}

	@Override
	public Operation bindEntityReference(EntityInstanceReference componentReference, EntityInstanceActual actualComponent) {
		return new OperationsSequence(
				operations.stream()
				.map(o -> o.bindEntityReference(componentReference, actualComponent))
				.collect(Collectors.toList()));
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		for (Operation o : operations)
			code += o.hashCode();
		return code;
	}

	@Override
	public void execute() {
		operations.stream().forEach(Operation::execute);
	}
	
	@Override
	public void clearCache() {
		operations.stream().forEach(Operation::clearCache);
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
	
	protected List<String> getOperationsString() {
		return operations.stream().map(Operation::toString).collect(Collectors.toList());
	}
	
	public String toString() {
		return "(" + String.join("; ", getOperationsString()) + ")";
	}

}
