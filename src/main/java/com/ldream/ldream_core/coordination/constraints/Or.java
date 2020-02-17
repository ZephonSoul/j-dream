package com.ldream.ldream_core.coordination.constraints;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Or extends AbstractEnnaryFormula implements Formula {

	public Or(Formula... subformulas) {
		super(subformulas);
	}

	public Or(List<Formula> subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {
		boolean sat = false;
		for (Formula subformula : subformulas) {
			sat = sat || subformula.sat(i);
			if (sat)
				break;
		}
		return sat;
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Or(subformulas.stream()
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
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
