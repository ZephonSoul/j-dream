package com.ldream.ldream_core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.constraints.Tautology;

public class AndR extends AbstractPILRule implements Rule {
	
	private static final int BASE_CODE = 30;

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
					.collect(Collectors.toSet()));
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
					.collect(Collectors.toSet()));
	}

	protected String getConnectiveSymbol() {
		return "&";
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof AndR) && equalSubRules((AndR) rule);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
