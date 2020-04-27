package com.dream.core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.predicates.Tautology;

public class AndRule extends AbstractRule implements Rule {
	
	private static final int BASE_CODE = 30;

	public AndRule(Set<Rule> rules) {
		super(rules);
	}
	
	public AndRule(Rule... rules) {
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
			return new AndRule(
					rules.stream()
					.map(Rule::expandDeclarations)
					.collect(Collectors.toSet()));
	}

//	@Override
//	public Rule bindEntityReference(
//			EntityInstanceRef componentVariable, 
//			EntityInstanceActual actualComponent) {
//
//		if (rules.isEmpty())
//			return new Term(Tautology.getInstance());
//		else
//			return new AndRule(rules.stream()
//					.map(r -> r.bindInstance(componentVariable, actualComponent))
//					.collect(Collectors.toSet()));
//	}

	protected String getConnectiveSymbol() {
		return "&";
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof AndRule) && equalSubRules((AndRule) rule);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

	@Override
	public <I> Rule bindInstance(Instance<I> reference, Instance<I> actual) {
		if (rules.isEmpty())
			return new Term(Tautology.getInstance());
		else
			return new AndRule(rules.stream()
					.map(r -> r.bindInstance(reference, actual))
					.collect(Collectors.toSet()));
	}

}
