package com.dream.core.coordination;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.operations.Operation;
import com.dream.core.operations.OperationsSet;
import com.dream.core.operations.Skip;

/**
 * @author Alessandro Maggi
 *
 */
public class ConjunctiveTerm implements Rule  {

	Formula constraint;
	Formula requirement;
	Operation operation;
	private Interaction cachedInteraction;
	private boolean cachedSat;

	public ConjunctiveTerm(
			Formula constraint,
			Formula requirement,
			Operation operation) {

		this.constraint = constraint;
		this.requirement = requirement;
		this.operation = operation;
	}

	public ConjunctiveTerm(Formula constraint,Formula requirement) {
		this(constraint,requirement,Skip.getInstance());
	}

	public ConjunctiveTerm(Formula constraint,Operation ops) {
		this(constraint,Tautology.getInstance(),ops);
	}

	public ConjunctiveTerm(Formula constraint) {
		this(constraint,Skip.getInstance());
	}

	public ConjunctiveTerm(Operation ops) {
		this(Tautology.getInstance(),ops);
	}

	/**
	 * @return the constraint
	 */
	public Formula getConstraint() {
		return constraint;
	}

	/**
	 * @return the requirement
	 */
	public Formula getRequirement() {
		return requirement;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	@Override
	public boolean sat(Interaction i) {
		if (!i.equals(cachedInteraction)) {
			cachedSat = requirement.sat(i) || !(constraint.sat(i));
			cachedInteraction = i;
		}
		return cachedSat;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		if (!i.equals(cachedInteraction)) {
			cachedSat = requirement.sat(i) || !(constraint.sat(i));
			cachedInteraction = i;
		}
		if (cachedSat && constraint.sat(i))
			return new OperationsSet(operation);
		else
			return new OperationsSet();
	}

//	@Override
//	public Rule bindEntityReference(
//			EntityInstanceRef componentReference, 
//			EntityInstanceActual actualComponent) {
//
//		return new ConjunctiveTerm(
//				constraint.bindInstance(componentReference,actualComponent),
//				requirement.bindInstance(componentReference,actualComponent),
//				operation.bindInstance(componentReference,actualComponent));
//	}

	@Override
	public Rule expandDeclarations() {
		return this;
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof ConjunctiveTerm)
				&& constraint.equals(((ConjunctiveTerm) rule).getConstraint())
				&& requirement.equals(((ConjunctiveTerm) rule).getRequirement())
				&& operation.equals(((ConjunctiveTerm) rule).getOperation());
	}

	@Override
	public void clearCache() {
		cachedInteraction = null;
		constraint.clearCache();
		requirement.clearCache();
		operation.clearCache();
	}

	public String toString() {
		return String.format("(%s ► %s -> %s)", 
				constraint.toString(), 
				requirement.toString(),
				operation.toString());
	}

	@Override
	public int hashCode() {
		return constraint.hashCode() + requirement.hashCode() + operation.hashCode();
	}

	@Override
	public <I> Rule bindInstance(Instance<I> reference, Instance<I> actual) {
		return new ConjunctiveTerm(
				constraint.bindInstance(reference,actual),
				requirement.bindInstance(reference,actual),
				operation.bindInstance(reference,actual));
	}
	
}
