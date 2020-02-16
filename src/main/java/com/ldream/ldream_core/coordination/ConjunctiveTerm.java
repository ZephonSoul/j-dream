package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.coordination.interactions.Formula;
import com.ldream.ldream_core.coordination.interactions.Tautology;
import com.ldream.ldream_core.coordination.operations.Operation;
import com.ldream.ldream_core.coordination.operations.OperationsSet;
import com.ldream.ldream_core.coordination.operations.Skip;

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
	

	public ConjunctiveTerm(Formula constraint,Operation ops) {
		this(constraint,new Tautology(),ops);
	}
	
	public ConjunctiveTerm(Formula constraint) {
		this(constraint,new Skip());
	}
	
	public ConjunctiveTerm(Operation ops) {
		this(new Tautology(),ops);
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

	@Override
	public Rule bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		return new ConjunctiveTerm(
				constraint.bindActualComponent(componentReference,actualComponent),
				requirement.bindActualComponent(componentReference,actualComponent),
				operation.bindActualComponent(componentReference,actualComponent));
	}
	
	@Override
	public Rule expandDeclarations() {
		return this;
	}
	
	public String toString() {
		return String.format("(%s ► %s -> %s)", 
				constraint.toString(), 
				requirement.toString(),
				operation.toString());
	}

}
