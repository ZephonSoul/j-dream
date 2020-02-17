package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.coordination.constraints.Tautology;
import com.ldream.ldream_core.coordination.operations.Operation;
import com.ldream.ldream_core.coordination.operations.OperationsSet;
import com.ldream.ldream_core.coordination.operations.Skip;

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
	public Rule bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return new Term(constraint.bindActualComponent(componentReference,actualComponent),
				operation.bindActualComponent(componentReference,actualComponent));
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

}
