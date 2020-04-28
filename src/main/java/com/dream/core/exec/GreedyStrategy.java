package com.dream.core.exec;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;

/**
 * @author Alessandro Maggi
 *
 */
public class GreedyStrategy implements ExecutionStrategy {

	private static ExecutionStrategy instance;
	
	public GreedyStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new GreedyStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(CoordinatingEntity rootComponent) {
		return rootComponent.getAllowedInteraction();
	}

}
