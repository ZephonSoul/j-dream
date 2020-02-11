package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;
import com.ldream.ldream_core.coordination.Interaction;

public interface Formula {

	boolean sat(Interaction i);

	Formula instantiateComponentVariable(ComponentVariable componentVariable, Component actualComponent);
	
}
