package com.ldream.ldream_core.exec;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.NoAdmissibleInteractionsException;
import com.ldream.ldream_core.coordination.Interaction;

public interface ExecutionStrategy {

	public Interaction selectInteraction(Component rootComponent) throws NoAdmissibleInteractionsException ;
	
}
