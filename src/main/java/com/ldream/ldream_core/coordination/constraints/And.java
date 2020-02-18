package com.ldream.ldream_core.coordination.constraints;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class And extends AbstractEnnaryFormula implements Formula {

	public And(Set<Formula> subformulas) {
		super(subformulas);
	}
	
	public And(Formula... subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {
		return subformulas.stream().allMatch(f -> f.sat(i));
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new And(subformulas.stream()
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toSet()));
	}

	public String getConnectiveSymbol() {
		return "Ʌ";
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof And)
				&& equalSubformulas((And) formula);
	}

}
