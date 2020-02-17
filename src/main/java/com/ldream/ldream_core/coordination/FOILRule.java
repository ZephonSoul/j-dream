package com.ldream.ldream_core.coordination;

import java.util.Arrays;

import com.ldream.ldream_core.coordination.constraints.Contradiction;
import com.ldream.ldream_core.coordination.constraints.Tautology;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public class FOILRule implements Rule {

	private Declaration declaration;
	private Rule rule;
	private Rule ruleInstance;

	public FOILRule(Declaration declaration,Rule rule) {
		this.declaration = declaration;
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}

	private Declaration getDeclaration() {
		return declaration;
	}

	@Override
	public Rule expandDeclarations() {
		ActualComponentInstance[] matchingInstances = 
				declaration.getActualComponents();

		switch(declaration.getQuantifier()) {
		case FORALL:
			if (matchingInstances.length == 0)
				ruleInstance = new Term(Tautology.getInstance());
			else
				ruleInstance = new AndR(Arrays.stream(matchingInstances)
						.map(c -> rule.bindActualComponent(declaration.getVariable(), c))
						.toArray(Rule[]::new));
			break;
		case EXISTS:
			if (matchingInstances.length == 0)
				ruleInstance = new Term(Contradiction.getInstance());
			else
				ruleInstance = new OrR(Arrays.stream(matchingInstances)
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
	public void clearCache() {
		ruleInstance = null;
		rule.clearCache();
	}

	@Override
	public String toString() {
		return String.format("%s{%s}",
				declaration.toString(),
				rule.toString());
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof FOILRule)
			&& declaration.equals(((FOILRule) rule).getDeclaration())
			&& rule.equals(((FOILRule) rule).getRule());
	}

}
