package com.dream.core.exec;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.NoAdmissibleInteractionsException;

/**
 * @author Alessandro Maggi
 *
 */
public interface ExecutionStrategy {

	public Interaction selectInteraction(CoordinatingEntity rootEntity) throws NoAdmissibleInteractionsException;
	
}
