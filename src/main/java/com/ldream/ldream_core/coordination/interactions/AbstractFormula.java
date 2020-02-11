package com.ldream.ldream_core.coordination.interactions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractFormula implements Formula {

	List<Formula> subformulas;

	public AbstractFormula(Formula... subformulas) {
		this.subformulas = Arrays.asList(subformulas);
	}
	
	public AbstractFormula(List<Formula> subformulas) {
		this.subformulas = subformulas;
	}

	public String toString() {
		return "(" + 
				subformulas.stream().map(Formula::toString).collect(Collectors.joining(getConnectiveSymbol()))
				+ ")";
	}

	@Override
	public abstract boolean sat(Interaction i);

	protected abstract String getConnectiveSymbol();

}
