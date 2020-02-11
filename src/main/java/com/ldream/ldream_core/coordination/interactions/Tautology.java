package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class Tautology implements Formula {

	@Override
	public boolean sat(Interaction i) {
		return true;
	}
	
	public String toString() {
		return "TRUE";
	}

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, Component actualComponent) {
		return this;
	}

}
