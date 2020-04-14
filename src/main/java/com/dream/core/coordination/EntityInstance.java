package com.dream.core.coordination;

import com.dream.core.Bindable;
import com.dream.core.Entity;

public interface EntityInstance extends Bindable<EntityInstance> {
	
	public String getName();
	
	public Entity getActualEntity();
	
}
