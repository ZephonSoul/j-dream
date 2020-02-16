package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Not implements Formula {
	
	private Formula subformula;

	public Not(Formula subformula) {
		this.subformula = subformula;
	}

	/**
	 * @return the subformula
	 */
	public Formula getSubformula() {
		return subformula;
	}

	@Override
	public boolean sat(Interaction i) {
		return !(subformula.sat(i));
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {
		
		return new Not(subformula.bindActualComponent(componentVariable, actualComponent));
	}
	
	@Override
	public String toString() {
		return "¬" + subformula.toString();
	}
	
	@Override
	public boolean equals(Formula formula) {
		if (formula instanceof Not)
			return subformula.equals(((Not) formula).getSubformula());
		else
			return false;
	}

}
