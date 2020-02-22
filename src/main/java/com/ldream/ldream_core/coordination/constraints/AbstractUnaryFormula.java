package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractUnaryFormula extends AbstractFormula implements Formula {
	
	Formula subformula;
	
	public AbstractUnaryFormula(Formula subformula) {
		this.subformula = subformula;
	}
	
	public Formula getSubformula() {
		return subformula;
	}

	@Override
	public abstract boolean sat(Interaction i);

	public boolean equalSubformula(AbstractUnaryFormula formula) {
		return subformula.equals(formula.getSubformula());
	}

	@Override
	public void clearCache() {
		subformula.clearCache();
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode() + subformula.hashCode();
	}

}
