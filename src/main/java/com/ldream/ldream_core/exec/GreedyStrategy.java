package com.ldream.ldream_core.exec;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.Interaction;

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
