package com.ldream.ldream_core.coordination.operations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractOperation implements Operation {
	
	List<Operation> operations;

	public AbstractOperation(Operation... operations) {
		this.operations = Arrays.asList(operations);
	}
	
	public AbstractOperation(List<Operation> operations) {
		this.operations = operations;
	}
	
	public List<Operation> getOperations() {
		return operations;
	}
	
	protected List<String> getOperationsString() {
		return operations.stream().map(Operation::toString).collect(Collectors.toList());
	}

	@Override
	public void evaluateParams() {
		operations.stream().forEach(Operation::evaluateParams);
//		for (Operation op : operations) {
//			op.evaluateParams();
//		}
	}

	@Override
	public void execute() {
		operations.stream().forEach(Operation::execute);
//		for (Operation op : operations) {
//			op.execute();
//		}
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		for (Operation o : operations)
			code += o.hashCode();
		return code;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Operation)
			return equals((Operation) o);
		else
			return false;
	}
	
	public boolean equals(Operation op) {
		if (this.getClass().equals(op.getClass())) {
			List<Operation> opList = ((AbstractOperation)op).getOperations();
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

}
