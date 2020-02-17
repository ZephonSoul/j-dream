package com.ldream.ldream_core.coordination;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.operations.OperationsSet;

public abstract class AbstractPILRule implements Rule {

	Rule[] rules;
	Interaction cachedInteraction;
	boolean cachedSat;
	
	public AbstractPILRule(Rule... rules) {
		this.rules = rules;
	}

	private Rule[] getRules() {
		return rules;
	}
	
	@Override
	public boolean sat(Interaction i) {
		if (i.equals(cachedInteraction))
			return cachedSat;
		else
			return checkSat(i);
	}
	
	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		OperationsSet executableOps = new OperationsSet();
		if (!i.equals(cachedInteraction)) {
			cachedSat = checkSat(i);
			cachedInteraction = i;
		}
		if (cachedSat) {
			for (Rule rule : rules) {
				executableOps.addOperationsSet(rule.getOperationsForInteraction(i));
			}
		}
		return executableOps;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Rule) && equals((Rule) o);
	}
	
	@Override
	public void clearCache() {
		Arrays.stream(rules).forEach(Rule::clearCache);
	}
	
	protected boolean equalSubRules(AbstractPILRule rule) {
		return rules.equals(rule.getRules());
	}

	public String toString() {
		return "(" + Arrays.stream(rules).map(Rule::toString)
				.collect(Collectors.joining(" " + getConnectiveSymbol() +" ")) + ")";
	}
	
	protected abstract boolean checkSat(Interaction i);
	
	protected abstract String getConnectiveSymbol();

}
