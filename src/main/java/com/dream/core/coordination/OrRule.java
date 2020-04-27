package com.dream.core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.predicates.Contradiction;

public class OrRule extends AbstractRule implements Rule {
	
	private static final int BASE_CODE = 11;

	public OrRule(Set<Rule> rules) {
		super(rules);
	}
	
	public OrRule(Rule... rules) {
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
			return new OrRule(
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
//			return new Term(Contradiction.getInstance());
//		else
//			return new OrRule(rules.stream()
//					.map(r -> r.bindInstance(componentVariable, actualComponent))
//					.collect(Collectors.toSet()));
//	}

	@Override
	protected String getConnectiveSymbol() {
		return "||";
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof OrRule) 	&& equalSubRules((OrRule) rule);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

	@Override
	public <I> Rule bindInstance(Instance<I> reference, Instance<I> actual) {
		if (rules.isEmpty())
			return new Term(Contradiction.getInstance());
		else
			return new OrRule(rules.stream()
					.map(r -> r.bindInstance(reference, actual))
					.collect(Collectors.toSet()));
	}

}
