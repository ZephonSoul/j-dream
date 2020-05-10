package com.dream.core.coordination.constraints;

import com.dream.core.Bindable;
import com.dream.core.Caching;
import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
public interface Formula extends Bindable<Formula>, Caching {

	public boolean sat(Interaction i);
	
	public boolean sat();
	
	public boolean equals(Formula formula);
	
	public void evaluateExpressions();
	
}
