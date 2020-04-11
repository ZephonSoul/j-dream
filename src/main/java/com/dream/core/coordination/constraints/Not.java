package com.dream.core.coordination.constraints;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.Interaction;

public class Not extends AbstractUnaryFormula implements Formula {
	
	private static final int BASE_CODE = 0;

	public Not(Formula subformula) {
		super(subformula);
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
	public boolean sat() {
		return !(subformula.sat());
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Not(subformula.bindActualComponent(componentVariable, actualComponent));
	}

	@Override
	public String toString() {
		return String.format("¬(%s)",subformula.toString());
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE - 2*(subformula.hashCode());
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Not)
				&& equalSubformula((Not) formula);
	}

}
