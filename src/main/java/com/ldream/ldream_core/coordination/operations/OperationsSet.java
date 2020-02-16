package com.ldream.ldream_core.coordination.operations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OperationsSet {

	private Set<Operation> operations;

	public OperationsSet() {
		operations = new HashSet<Operation>();
	}

	public OperationsSet(Set<Operation> operations) {
		this.operations = operations;
	}

	public OperationsSet(Operation operation) {
		this();
		operations.add(operation);
	}

	public OperationsSet(Operation... operations) {
		this(Arrays.asList(operations).stream().collect(Collectors.toSet()));
	}

	public Set<Operation> getOperations() {
		return operations;
	}

	public boolean addOperation(Operation operation) {
		return operations.add(operation);
	}

	public boolean addOperationsSet(OperationsSet opSet) {
		return operations.addAll(opSet.getOperations());
	}

	public void executeOperations(boolean snapshotSemantics) {
		if (snapshotSemantics) {
			//operations.stream().map(o -> {o.evaluateParams(); return o;}).forEach(Operation::execute);
			operations.stream().forEach(Operation::evaluateOperands);
			operations.stream().forEach(Operation::execute);
		} else {
			operations.stream().forEach(o -> {o.evaluateOperands(); o.execute();});
		}
	}

	@Override
	public int hashCode() {
		return operations.stream().mapToInt(Operation::hashCode).sum();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof OperationsSet)
				&& equals((OperationsSet) o);
	}

	public boolean equals(OperationsSet opSet) {
		return operations.size() == opSet.getOperations().size()
				&& operations.containsAll(opSet.getOperations());
	}

	public String toString() {
		return "{" + String.join(",", operations.stream().map(Operation::toString).toArray(String[]::new)) + "}";
	}

	public int size() {
		return operations.size();
	}

}
