package com.ldream.ldream_core.coordination.interactions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractEnnaryFormula extends AbstractFormula implements Formula {

	List<Formula> subformulas;

	public AbstractEnnaryFormula(Formula... subformulas) {
		this.subformulas = Arrays.asList(subformulas);
	}
	
	public AbstractEnnaryFormula(List<Formula> subformulas) {
		this.subformulas = subformulas;
	}
	
	public List<Formula> getSubformulas() {
		return subformulas;
	}

	public String toString() {
		return "(" + 
				subformulas.stream().map(Formula::toString).collect(Collectors.joining(getConnectiveSymbol()))
				+ ")";
	}
	
	@Override
	public void clearCache() {
		subformulas.stream().forEach(Formula::clearCache);
	}
	
	public boolean equalSubformulas(AbstractEnnaryFormula formula) {
		return subformulas.equals(formula.getSubformulas());
	}

	@Override
	public abstract boolean sat(Interaction i);

	protected abstract String getConnectiveSymbol();

}
