package com.ldream.ldream_core.coordination.constraints;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractEnnaryFormula extends AbstractFormula implements Formula {

	Set<Formula> subformulas;

	public AbstractEnnaryFormula(Set<Formula> subformulas) {
		this.subformulas = subformulas;
	}
	
	public AbstractEnnaryFormula(Formula... subformulas) {
		this.subformulas = Arrays.stream(subformulas).collect(Collectors.toSet());
	}

	public Set<Formula> getSubformulas() {
		return subformulas;
	}

	public String toString() {
		return "(" + 
				subformulas.stream()
		.map(Formula::toString)
		.collect(Collectors.joining(" " + getConnectiveSymbol() + " "))
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
	public int hashCode() {
		return this.getClass().hashCode() +
				subformulas.stream().mapToInt(Formula::hashCode).sum();
	}

	@Override
	public abstract boolean sat(Interaction i);

	protected abstract String getConnectiveSymbol();

}
