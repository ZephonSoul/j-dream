package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.constraints.Formula;

public class Tautology extends AbstractConstantPredicate implements Formula {
	
	private static final int BASE_CODE = 1;
	
	private static Tautology instance;
	
	public static Tautology getInstance() {
		if (instance == null)
			instance = new Tautology();
		return instance;
	}

	@Override
	public boolean sat() {
		return true;
	}
	
	public String toString() {
		return "TRUE";
	}
	
	@Override
	public boolean equals(Formula formula) {
		return formula instanceof Tautology;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
