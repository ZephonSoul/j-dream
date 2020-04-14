package com.dream.core.exec;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;

public class MaxInteractionStrategy implements ExecutionStrategy {

	private static ExecutionStrategy instance;
	
	public MaxInteractionStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new MaxInteractionStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(CoordinatingEntity rootComponent) {
		Interaction[] interactions = rootComponent.getAllowedInteractions();
		int maxSize = 0, index = 0;
		for (int i=0; i<interactions.length; i++)
			if (interactions[i].size() > maxSize) {
				index = i;
				maxSize = interactions[i].size();
			}
		return interactions[index];
	}

}
