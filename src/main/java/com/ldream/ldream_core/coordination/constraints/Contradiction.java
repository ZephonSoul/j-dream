package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.coordination.Interaction;

public class Contradiction extends AbstractConstantFormula implements Formula {
	
	private static Contradiction instance;
	
	public static Contradiction getInstance() {
		if (instance == null)
			instance = new Contradiction();
		return instance;
	}

	@Override
	public boolean sat(Interaction i) {
		return false;
	}
	
	public String toString() {
		return "FALSE";
	}
	
	@Override
	public boolean equals(Formula formula) {
		return formula instanceof Tautology;
	}

}
