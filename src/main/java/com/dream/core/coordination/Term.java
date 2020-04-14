package com.dream.core.coordination;

import com.dream.core.coordination.constraints.Formula;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.coordination.operations.Operation;
import com.dream.core.coordination.operations.OperationsSet;
import com.dream.core.coordination.operations.Skip;

public class Term implements Rule  {

	Formula constraint;
	Operation operation;
	private Interaction cachedInteraction;
	private boolean cachedSat;

	public Term(Formula constraint,Operation ops) {
		this.constraint = constraint;
		this.operation = ops;
	}

	public Term(Formula constraint) {
		this(constraint,Skip.getInstance());
	}

	public Term(Operation ops) {
		this(Tautology.getInstance(),ops);
	}

	public Operation getOperation() {
		return operation;
	}

	public Object getConstraint() {
		return constraint;
	}

	@Override
	public boolean sat(Interaction i) {
		if (!i.equals(cachedInteraction)) {
			cachedSat = constraint.sat(i);
			cachedInteraction = i;
		}
		return cachedSat;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		if (!i.equals(cachedInteraction)) {
			cachedSat = constraint.sat(i);
			cachedInteraction = i;
		}
		if (cachedSat)
			return new OperationsSet(operation);
		else
			return new OperationsSet();
	}

	@Override
	public Rule bindEntityReference(
			EntityInstanceReference componentReference, 
			EntityInstanceActual actualComponent) {

		return new Term(constraint.bindEntityReference(componentReference,actualComponent),
				operation.bindEntityReference(componentReference,actualComponent));
	}

	@Override
	public Rule expandDeclarations() {
		return this;
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof Term)
				&& constraint.equals(((Term) rule).getConstraint())
				&& operation.equals(((Term) rule).getOperation());
	}

	@Override
	public void clearCache() {
		cachedInteraction = null;
		constraint.clearCache();
		operation.clearCache();
	}

	public String toString() {
		return String.format("(%s -> %s)", constraint.toString(), operation.toString());
	}
	
	@Override
	public int hashCode() {
		return constraint.hashCode() + operation.hashCode();
	}

}
