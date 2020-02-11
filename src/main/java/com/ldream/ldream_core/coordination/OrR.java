package com.ldream.ldream_core.coordination;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.interactions.Not;
import com.ldream.ldream_core.coordination.interactions.Tautology;

public class OrR extends AbstractPILRule implements Rule {

	public OrR(Rule... rules) {
		super(rules);
	}

	public OrR(List<Rule> rules) {
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
	public Rule bindActualComponent(
			ReferencedComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		if (rules.isEmpty())
			return new Term(new Not(new Tautology()));
		else
			return new OrR(rules.stream()
					.map(r -> r.bindActualComponent(componentVariable, actualComponent))
					.collect(Collectors.toList()));
	}

	@Override
	protected String getConnectiveSymbol() {
		return "\\/";
	}

}
