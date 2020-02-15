package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.coordination.interactions.Formula;
import com.ldream.ldream_core.coordination.interactions.Tautology;
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
		this(constraint,new Skip());
	}
	
	public Term(Operation ops) {
		this(new Tautology(),ops);
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
	
	public String toString() {
		return String.format("(%s -> %s)", constraint.toString(), operation.toString());
	}

}
