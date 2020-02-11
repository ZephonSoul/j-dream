package com.ldream.ldream_core.coordination;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;

public class AndR extends AbstractPILRule implements Rule {

	public AndR(Rule... rules) {
		super(rules);
	}
	
	public AndR(List<Rule> rules) {
		super(rules);
	}

	@Override
	public boolean checkSat(Interaction i) {
		boolean sat = true;
		for (Rule rule : rules) {
			if (!rule.sat(i)) {
				sat = false;
				break;
			}
		}
		cachedInteraction = i;
		cachedSat = sat;
		return sat;
	}

	@Override
	public Rule instantiateComponentVariable(
			ComponentVariable componentVariable, 
			Component actualComponent) {
		
		return new AndR(rules.stream()
				.map(r -> r.instantiateComponentVariable(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

	protected String getConnectiveSymbol() {
		return "/\\";
	}

}
