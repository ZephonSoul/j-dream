package com.dream.core.coordination.constraints.predicates;

import com.dream.core.coordination.constraints.Formula;

/**
 * @author Alessandro Maggi
 *
 */
public class Contradiction extends AbstractConstantPredicate implements Predicate {
	
	private static final int BASE_CODE = -1;
	
	private static Contradiction instance;
	
	public static Contradiction getInstance() {
		if (instance == null)
			instance = new Contradiction();
		return instance;
	}

	@Override
	public boolean sat() {
		return false;
	}
	
	public String toString() {
		return "FALSE";
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
