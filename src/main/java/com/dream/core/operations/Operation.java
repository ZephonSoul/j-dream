package com.dream.core.operations;

import com.dream.core.Bindable;
import com.dream.core.Caching;

/**
 * @author Alessandro Maggi
 *
 */
public interface Operation extends Bindable<Operation>,Caching {

	public void evaluate();
	
	public void execute();
	
	public boolean equals(Operation op);
	
}
