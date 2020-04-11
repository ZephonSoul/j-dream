package com.dream.exec;

import com.dream.core.components.Component;
import com.dream.core.components.NoAdmissibleInteractionsException;
import com.dream.core.coordination.Interaction;

public interface ExecutionStrategy {

	public Interaction selectInteraction(Component rootComponent) throws NoAdmissibleInteractionsException ;
	
}
