package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class And extends AbstractFormula implements Formula {

	public And(Formula... subformulas) {
		super(subformulas);
	}

	public And(List<Formula> subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {

		boolean sat = true;
		for (Formula subformula : subformulas) {
			sat = sat && subformula.sat(i);
			if (!sat)
				break;
		}
		return sat;
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new And(subformulas.stream()
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}

	public String getConnectiveSymbol() {
		return "/\\";
	}

	@Override
	public boolean equals(Formula formula) {
		if (formula instanceof And)
			return equalSubformulas((And) formula);
		else
			return false;
	}

}
