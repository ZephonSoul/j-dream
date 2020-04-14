package com.dream.test;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.dream.core.AbstractEntity;
import com.dream.core.coordination.Interaction;
import com.dream.core.entities.AbstractCoordinatingEntity;

public class CoordinatingEntityTest extends AbstractCoordinatingEntity {

	public CoordinatingEntityTest() {
		super();
	}
	
	public static void main(String[] args) {
		AbstractCoordinatingEntity e1 = new CoordinatingEntityTest();
		AbstractEntity	e2 = new InteractingEntityTest(),
				e3 = new InteractingEntityTest();
		e1.setPool(e2,e3);
		
		System.out.println(e1.getJSONDescriptor().toString());


		//System.out.println(Arrays.stream(e1.getAllowedInteractions()).map(Interaction::toString).collect(Collectors.joining("\n")));
		//System.out.println(e1.getOperationsForInteraction(e1.getAllowedInteraction()).toString());
		for (int i=0; i<11; i++)
			System.out.println(e1.getAllowedInteraction().toString());
	}

}
