package com.dream.test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.AbstractInteractingEntity;
import com.dream.core.entities.Port;
import com.dream.core.entities.Store;
import com.dream.core.operations.OperationsSet;

public class InteractingEntityTest extends AbstractInteractingEntity {

	public InteractingEntityTest() {
		super();
		Port[] ports = {new Port("p"),new Port("k")};
		setInterface(Arrays.stream(ports).collect(Collectors.toSet()));
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		return new OperationsSet();
	}

}
