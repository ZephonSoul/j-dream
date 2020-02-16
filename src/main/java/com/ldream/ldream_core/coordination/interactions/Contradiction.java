package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Contradiction implements Formula {
	
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
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}

}
