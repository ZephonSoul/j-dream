package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Tautology implements Formula {
	
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
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}

}
