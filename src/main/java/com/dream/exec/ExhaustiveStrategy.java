package com.dream.exec;

import com.dream.core.components.Component;
import com.dream.core.components.NoAdmissibleInteractionsException;
import com.dream.core.coordination.Interaction;

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
		if (interactions.length == 0)
			throw new NoAdmissibleInteractionsException(rootComponent);
		return interactions[(int) Math.floor(Math.random()*interactions.length)];
	}

}
