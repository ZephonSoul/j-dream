package com.dream.core.coordination.constraints;

import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
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
	
	@Override
	public void evaluateExpressions() {
		subformula.evaluateExpressions();
	}

}
