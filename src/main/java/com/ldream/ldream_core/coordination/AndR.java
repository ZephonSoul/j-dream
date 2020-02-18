package com.ldream.ldream_core.coordination;

import java.util.Set;

import com.ldream.ldream_core.coordination.constraints.Tautology;

public class AndR extends AbstractPILRule implements Rule {

	public AndR(Set<Rule> rules) {
		super(rules);
	}
	
	public AndR(Rule... rules) {
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
	public Rule expandDeclarations() {
		if (rules.isEmpty())
			return new Term(Tautology.getInstance());
		else
			return new AndR(
					rules.stream()
					.map(Rule::expandDeclarations)
					.toArray(Rule[]::new));
	}

	@Override
	public Rule bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		if (rules.isEmpty())
			return new Term(Tautology.getInstance());
		else
			return new AndR(rules.stream()
					.map(r -> r.bindActualComponent(componentVariable, actualComponent))
					.toArray(Rule[]::new));
	}

	protected String getConnectiveSymbol() {
		return "&";
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof AndR) && equalSubRules((AndR) rule);
	}

}
