package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;
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
	public Rule getPILRule() {
		switch(declaration.getQuantifier()) {
		case FORALL:
			ruleInstance = new AndR(declaration.getActualComponents().stream()
					.map(c -> rule.bindActualComponent(declaration.getVariable(), c)).toArray(Rule[]::new));
			break;
		case EXISTS:
			ruleInstance = new OrR(declaration.getActualComponents().stream()
					.map(c -> rule.bindActualComponent(declaration.getVariable(), c)).toArray(Rule[]::new));
			break;
		}
		return ruleInstance;
	}

	@Override
	public Rule bindActualComponent(ComponentInstance componentInstance,Component actualComponent) {
		ruleInstance = getPILRule().bindActualComponent(componentInstance, actualComponent);
		return ruleInstance;
	}

	@Override
	public boolean sat(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = getPILRule();
		return ruleInstance.sat(i);
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = getPILRule();
		return ruleInstance.getOperationsForInteraction(i);
	}
	
	@Override
	public String toString() {
		return String.format("%s{%s}",
				declaration.toString(),
				rule.toString());
	}
	
}
