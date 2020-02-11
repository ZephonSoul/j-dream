package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public class FOILRule implements Rule {

	private ComponentVariable componentVariable;
	private Rule rule;
	private Rule ruleInstance;
	
	public FOILRule(ComponentVariable componentVariable,Rule rule) {
		this.componentVariable = componentVariable;
		this.rule = rule;
	}

	@Override
	public Rule getPILRule() {
		switch(componentVariable.getQuantifier()) {
		case FORALL:
			ruleInstance = new AndR(componentVariable.getActualComponents().stream()
					.map(c -> rule.instantiateComponentVariable(componentVariable, c)).toArray(Rule[]::new));
			break;
		case EXISTS:
			ruleInstance = new OrR(componentVariable.getActualComponents().stream()
					.map(c -> rule.instantiateComponentVariable(componentVariable, c)).toArray(Rule[]::new));
			break;
		}
		return ruleInstance;
	}

	@Override
	public Rule instantiateComponentVariable(ComponentVariable componentVariable,Component actualComponent) {
		ruleInstance = getPILRule().instantiateComponentVariable(componentVariable, actualComponent);
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
		return String.format("%s{%s}",componentVariable.toString(),rule.toString());
	}
	
}
