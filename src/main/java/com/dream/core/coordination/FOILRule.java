package com.dream.core.coordination;

/**
 * @author Alessandro Maggi
 *
 */
import java.util.Arrays;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.predicates.Contradiction;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.operations.OperationsSet;

public class FOILRule implements Rule {

	private Declaration declaration;
	private Rule rule;
	private Rule ruleInstance;

	public FOILRule(Declaration declaration,Rule rule) {
		this.declaration = declaration;
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}

	private Declaration getDeclaration() {
		return declaration;
	}

	@Override
	public Rule expandDeclarations() {
		EntityInstanceActual[] matchingInstances = 
				declaration.getActualEntities();

		switch(declaration.getQuantifier()) {
		case FORALL:
			if (matchingInstances.length == 0)
				ruleInstance = new Term(Tautology.getInstance());
			else
				ruleInstance = new AndRule(Arrays.stream(matchingInstances)
						.map(c -> rule.bindInstance(declaration.getVariable(), c))
						.collect(Collectors.toSet()));
			break;
		case EXISTS:
			if (matchingInstances.length == 0)
				ruleInstance = new Term(Contradiction.getInstance());
			else
				ruleInstance = new OrRule(Arrays.stream(matchingInstances)
						.map(c -> rule.bindInstance(declaration.getVariable(), c))
						.collect(Collectors.toSet()));
			break;
		}
		return ruleInstance;
	}

//	@Override
//	public Rule bindEntityReference(
//			EntityInstanceRef componentReference,
//			EntityInstanceActual actualComponent) {
//
//		ruleInstance = new FOILRule(
//				declaration.bindEntityReference(componentReference, actualComponent),
//				rule
//				);
//		ruleInstance = ruleInstance.expandDeclarations().bindInstance(componentReference, actualComponent);
//		return ruleInstance;
//	}

	@Override
	public boolean sat(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = expandDeclarations();
		return ruleInstance.sat(i);
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		if (ruleInstance == null)
			ruleInstance = expandDeclarations();
		return ruleInstance.getOperationsForInteraction(i);
	}
	
	@Override
	public void clearCache() {
		ruleInstance = null;
		rule.clearCache();
		declaration.clearCache();
	}

	@Override
	public String toString() {
		return String.format("%s{%s}",
				declaration.toString(),
				rule.toString());
	}

	@Override
	public boolean equals(Rule rule) {
		return (rule instanceof FOILRule)
			&& declaration.equals(((FOILRule) rule).getDeclaration())
			&& rule.equals(((FOILRule) rule).getRule());
	}
	
	@Override
	public int hashCode() {
		return declaration.hashCode() + rule.hashCode();
	}

	@Override
	public <I> Rule bindInstance(Instance<I> reference, Instance<I> actual) {
		ruleInstance = new FOILRule(
				declaration.bindInstance(reference, actual),
				rule.bindInstance(reference,actual)
				);
		ruleInstance = ruleInstance.expandDeclarations();//.bindInstance(reference, actual);
		return ruleInstance;
	}

}
