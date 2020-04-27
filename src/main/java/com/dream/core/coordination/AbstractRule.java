package com.dream.core.coordination;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.operations.OperationsSet;

public abstract class AbstractRule implements Rule {

	Set<Rule> rules;
	Interaction cachedInteraction;
	boolean cachedSat;
	
	public AbstractRule(Set<Rule> rules) {
		this.rules = rules;
	}
	
	public AbstractRule(Rule... rules) {
		this.rules = Arrays.stream(rules).collect(Collectors.toSet());
	}

	private Set<Rule> getRules() {
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
		rules.stream().forEach(Rule::clearCache);
	}
	
	protected boolean equalSubRules(AbstractRule rule) {
		return rules.equals(rule.getRules());
	}

	public String toString() {
		return "(" + rules.stream().map(Rule::toString)
				.collect(Collectors.joining(" " + getConnectiveSymbol() +" ")) + ")";
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() +
				rules.stream().mapToInt(Rule::hashCode).sum();
	}
	
	protected abstract boolean checkSat(Interaction i);
	
	protected abstract String getConnectiveSymbol();

}
