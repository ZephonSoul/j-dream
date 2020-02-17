package com.ldream.ldream_core.coordination.constraints;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractEnnaryFormula extends AbstractFormula implements Formula {

	Formula[] subformulas;

	public AbstractEnnaryFormula(Formula... subformulas) {
		this.subformulas = subformulas;
	}

	public Formula[] getSubformulas() {
		return subformulas;
	}

	public String toString() {
		return "(" + 
				Arrays.stream(subformulas)
		.map(Formula::toString)
		.collect(Collectors.joining(" " + getConnectiveSymbol() + " "))
		+ ")";
	}

	@Override
	public void clearCache() {
		Arrays.stream(subformulas).forEach(Formula::clearCache);
	}

	public boolean equalSubformulas(AbstractEnnaryFormula formula) {
		return subformulas.equals(formula.getSubformulas());
	}

	@Override
	public abstract boolean sat(Interaction i);

	protected abstract String getConnectiveSymbol();

}
