package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

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
			ReferencedComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {
		
		return new And(subformulas.stream()
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}
	
	public String getConnectiveSymbol() {
		return "/\\";
	}

}
