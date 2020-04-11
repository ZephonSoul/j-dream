package com.dream.core.coordination.constraints;

import com.dream.core.Bindable;
import com.dream.core.coordination.Interaction;

public interface Formula extends Bindable<Formula> {

	public boolean sat(Interaction i);
	
	public boolean sat();
	
	public boolean equals(Formula formula);

	public void clearCache();
	
}
