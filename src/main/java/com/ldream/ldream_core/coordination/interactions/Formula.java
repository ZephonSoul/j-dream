package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.coordination.Interaction;

public interface Formula extends Bindable<Formula> {

	boolean sat(Interaction i);
	
}
