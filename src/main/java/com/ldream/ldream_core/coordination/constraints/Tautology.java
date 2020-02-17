package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.coordination.Interaction;

public class Tautology extends AbstractConstantFormula implements Formula {
	
	private static Tautology instance;
	
	public static Tautology getInstance() {
		if (instance == null)
			instance = new Tautology();
		return instance;
	}

	@Override
	public boolean sat(Interaction i) {
		return true;
	}
	
	public String toString() {
		return "TRUE";
	}
	
	@Override
	public boolean equals(Formula formula) {
		return formula instanceof Tautology;
	}

}