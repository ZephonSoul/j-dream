package com.dream.core.coordination;

/**
 * @author Alessandro Maggi
 *
 */
import com.dream.core.Bindable;
import com.dream.core.Caching;
import com.dream.core.operations.OperationsSet;

public interface Rule 
extends Bindable<Rule>, Caching {
	
	public boolean sat(Interaction i);
	
	public OperationsSet getOperationsForInteraction(Interaction i);
	
	public Rule expandDeclarations();
	
	public boolean equals(Rule rule);
	
	public int hashCode();
	
}
