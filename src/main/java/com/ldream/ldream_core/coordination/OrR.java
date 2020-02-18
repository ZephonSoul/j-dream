package com.ldream.ldream_core.coordination;

import java.util.Set;

import com.ldream.ldream_core.coordination.constraints.Contradiction;

public class OrR extends AbstractPILRule implements Rule {

	public OrR(Set<Rule> rules) {
		super(rules);
	}
	
	public OrR(Rule... rules) {
		super(rules);
	}

	@Override
	public boolean checkSat(Interaction i) {
		boolean sat = false;
		for (Rule rule : rules) {
			if (rule.sat(i)) {
				sat = true;
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
			return new Term(Contradiction.getInstance());
		else
			return new OrR(
					rules.stream()
					.map(Rule::expandDeclarations)
					.toArray(Rule[]::new));
	}

	@Override
	public Rule bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		if (rules.isEmpty())
			return new Term(Contradiction.getInstance());
		else
			return new OrR(rules.stream()
					.map(r -> r.bindActualComponent(componentVariable, actualComponent))
					.toArray(Rule[]::new));
	}

	@Override
	protected String getConnectiveSymbol() {
		return "||";
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof OrR) 	&& equalSubRules((OrR) rule);
	}

}
