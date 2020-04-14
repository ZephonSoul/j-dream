package com.dream.core.exec;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.NoAdmissibleInteractionsException;

public class ExhaustiveStrategy implements ExecutionStrategy {
	
	private static ExecutionStrategy instance;

	public ExhaustiveStrategy() {}
	
	public static ExecutionStrategy getInstance() {
		if (instance == null)
			instance = new ExhaustiveStrategy();
		return instance;
	}

	@Override
	public Interaction selectInteraction(CoordinatingEntity rootComponent) {
		Interaction[] interactions = rootComponent.getAllowedInteractions();
		if (interactions.length == 0)
			throw new NoAdmissibleInteractionsException(rootComponent);
		return interactions[(int) Math.floor(Math.random()*interactions.length)];
	}

}
