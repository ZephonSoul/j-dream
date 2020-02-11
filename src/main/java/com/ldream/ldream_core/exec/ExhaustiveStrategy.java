package com.ldream.ldream_core.exec;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.Interaction;

public class ExhaustiveStrategy implements ExecutionStrategy {
	
	private static ExecutionStrategy instance;

	public ExhaustiveStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new ExhaustiveStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(Component rootComponent) {
		Interaction[] interactions = rootComponent.getAllAllowedInteractions();
		return interactions[(int) Math.floor(Math.random()*interactions.length)];
	}

}
