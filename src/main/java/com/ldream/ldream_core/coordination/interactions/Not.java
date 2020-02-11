package com.ldream.ldream_core.coordination.interactions;

import java.util.List;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class Not extends AbstractFormula implements Formula {

	public Not(Formula subformula) {
		super(subformula);
	}
	
	public Not(List<Formula> subformula) {
		super(subformula);
	}

	@Override
	public boolean sat(Interaction i) {
		return !(subformulas.get(0).sat(i));
	}

	@Override
	public Formula bindActualComponent(
			ReferencedComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {
		
		return new Not(subformulas.stream().limit(1)
				.map(f -> f.bindActualComponent(componentVariable, actualComponent))
				.collect(Collectors.toList()));
	}
	
	@Override
	public String toString() {
		return getConnectiveSymbol() + subformulas.get(0).toString();
	}

	@Override
	protected String getConnectiveSymbol() {
		return "¬";
	}

}
