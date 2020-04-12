package com.ldream.ldream_core.exec;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.Interaction;

public class MaxInteractionStrategy implements ExecutionStrategy {

	private static ExecutionStrategy instance;
	
	public MaxInteractionStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new MaxInteractionStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(Component rootComponent) {
		Interaction[] interactions = rootComponent.getAllAllowedInteractions();
		int maxSize = 0, index = 0;
		for (int i=0; i<interactions.length; i++)
			if (interactions[i].size() > maxSize) {
				index = i;
				maxSize = interactions[i].size();
			}
		return interactions[index];
	}

}
