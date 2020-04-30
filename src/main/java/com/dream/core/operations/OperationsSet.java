package com.dream.core.operations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class OperationsSet implements Operation {

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

	public boolean addAllOperationsSet(OperationsSet opSet) {
		return operations.addAll(opSet.getOperations());
	}
	
	public boolean isEmpty() {
		return operations.isEmpty();
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
		return "{" + String.join(
				",", operations.stream().filter(
						o -> !(o.equals(Skip.getInstance())))
				.map(Operation::toString).toArray(String[]::new)) + "}";
	}

	public int size() {
		return operations.size();
	}

	@Override
	public <I> Operation bindInstance(Instance<I> reference, Instance<I> actual) {
		return new OperationsSet(
				operations.stream().map(
						o -> o.bindInstance(reference,actual)
						).collect(Collectors.toSet()));
	}

	@Override
	public void clearCache() {
		operations.stream().forEach(Operation::clearCache);
	}

	@Override
	public void evaluateOperands() {
		operations.stream().forEach(Operation::evaluateOperands);
	}

	@Override
	public void execute() {
		operations.stream().forEach(Operation::execute);
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof OperationsSet) &&
				((OperationsSet)op).getOperations().equals(operations);
	}

}
