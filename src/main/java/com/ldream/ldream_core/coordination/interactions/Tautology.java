package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class Tautology implements Formula {

	@Override
	public boolean sat(Interaction i) {
		return true;
	}
	
	public String toString() {
		return "TRUE";
	}

	@Override
	public Formula bindActualComponent(ReferencedComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}

}
