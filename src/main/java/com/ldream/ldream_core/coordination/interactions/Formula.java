package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.coordination.Interaction;

public interface Formula extends Bindable<Formula> {

	public boolean sat(Interaction i);
	
	public boolean equals(Formula formula);
	
}
