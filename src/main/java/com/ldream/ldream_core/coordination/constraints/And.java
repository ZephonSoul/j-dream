package com.ldream.ldream_core.coordination.constraints;

import java.util.Arrays;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class And extends AbstractEnnaryFormula implements Formula {

	public And(Formula... subformulas) {
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

		return new And(Arrays.stream(subformulas)
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.toArray(Formula[]::new));
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
