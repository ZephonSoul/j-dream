package com.ldream.ldream_core.coordination.constraints;

public class Contradiction extends AbstractConstantFormula implements Formula {
	
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
