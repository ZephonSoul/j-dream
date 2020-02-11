package com.ldream.ldream_core.coordination;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.interactions.Not;
import com.ldream.ldream_core.coordination.interactions.Tautology;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public abstract class AbstractPILRule implements Rule {

	List<Rule> rules;
	Interaction cachedInteraction;
	boolean cachedSat;
	
	public AbstractPILRule(Rule... rules) {
		this.rules = Arrays.asList(rules);
	}
	
	public AbstractPILRule(List<Rule> rules) {
		this.rules = rules;
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
	public Rule expandDeclarations() {
		if (rules.isEmpty())
			return new Term(new Not(new Tautology()));
		else
			return this;
	}
	
	public String toString() {
		return rules.stream().map(Rule::toString).collect(Collectors.joining(getConnectiveSymbol()));
	}
	
	protected abstract boolean checkSat(Interaction i);
	
	protected abstract String getConnectiveSymbol();

}
