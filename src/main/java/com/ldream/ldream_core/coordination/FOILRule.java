package com.ldream.ldream_core.coordination;

import java.util.Set;

import com.ldream.ldream_core.coordination.interactions.Not;
import com.ldream.ldream_core.coordination.interactions.Tautology;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public class FOILRule implements Rule {

	private Declaration declaration;
	private Rule rule;
	private Rule ruleInstance;

	public FOILRule(Declaration declaration,Rule rule) {
		this.declaration = declaration;
		this.rule = rule;
	}

	@Override
	public Rule expandDeclarations() {
		Set<ActualComponentInstance> matchingInstances = 
				declaration.getActualComponents();

		switch(declaration.getQuantifier()) {
		case FORALL:
			if (matchingInstances.isEmpty())
				ruleInstance = new Term(new Tautology());
			else
				ruleInstance = new AndR(declaration.getActualComponents().stream()
						.map(c -> rule.bindActualComponent(declaration.getVariable(), c))
						.toArray(Rule[]::new));
			break;
		case EXISTS:
			if (matchingInstances.isEmpty())
				ruleInstance = new Term(new Not(new Tautology()));
			else
				ruleInstance = new OrR(declaration.getActualComponents().stream()
						.map(c -> rule.bindActualComponent(declaration.getVariable(), c))
						.toArray(Rule[]::new));
			break;
		}
		return ruleInstance;
	}

	@Override
	public Rule bindActualComponent(
			ComponentInstance componentReference,
			ActualComponentInstance actualComponent) {

		ruleInstance = new FOILRule(
				declaration.bindActualComponent(componentReference, actualComponent),
				rule
				);
		ruleInstance = ruleInstance.expandDeclarations().bindActualComponent(componentReference, actualComponent);
		return ruleInstance;
	}

	@Override
	public boolean sat(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = expandDeclarations();
		return ruleInstance.sat(i);
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = expandDeclarations();
		return ruleInstance.getOperationsForInteraction(i);
	}

	@Override
	public String toString() {
		return String.format("%s{%s}",
				declaration.toString(),
				rule.toString());
	}

}
