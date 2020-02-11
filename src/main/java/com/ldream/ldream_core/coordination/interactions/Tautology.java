package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;
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
	public Formula instantiateComponentVariable(ComponentVariable componentVariable, Component actualComponent) {
		return this;
	}

}
