package com.ldream.ldream_core.coordination;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.interactions.Tautology;

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
	public Rule expandDeclarations() {
		if (rules.isEmpty())
			return new Term(Tautology.getInstance());
		else
			return new AndR(
					rules.stream().
					map(Rule::expandDeclarations).
					collect(Collectors.toList())	);
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
					.collect(Collectors.toList()));
	}

	protected String getConnectiveSymbol() {
		return "&";
	}

}
