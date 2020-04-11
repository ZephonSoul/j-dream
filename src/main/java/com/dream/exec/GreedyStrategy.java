package com.dream.exec;

import com.dream.core.components.Component;
import com.dream.core.coordination.Interaction;

public class GreedyStrategy implements ExecutionStrategy {

	private static ExecutionStrategy instance;
	
	public GreedyStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new GreedyStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(Component rootComponent) {
		return rootComponent.getAllowedInteraction();
	}

}
