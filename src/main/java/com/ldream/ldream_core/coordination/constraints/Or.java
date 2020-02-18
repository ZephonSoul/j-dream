package com.ldream.ldream_core.coordination.constraints;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Or extends AbstractEnnaryFormula implements Formula {

	public Or(Set<Formula> subformulas) {
		super(subformulas);
	}
	
	public Or(Formula... subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {
		return subformulas.stream().anyMatch(f -> f.sat(i));
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Or(subformulas.stream()
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toSet()));
	}

	public String getConnectiveSymbol() {
		return "V";
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Or)
				&& equalSubformulas((Or) formula);
	}

}
