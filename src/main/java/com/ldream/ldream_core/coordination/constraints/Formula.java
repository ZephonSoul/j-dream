package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.coordination.Interaction;

public interface Formula extends Bindable<Formula> {

	public boolean sat(Interaction i);
	
	public boolean sat();
	
	public boolean equals(Formula formula);

	public void clearCache();
	
}
