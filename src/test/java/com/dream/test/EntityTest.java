package com.dream.test;

import com.dream.core.AbstractEntity;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.operations.OperationsSet;

public class EntityTest extends AbstractEntity {

	public EntityTest() {
		super();
	}
	
	public EntityTest(AbstractEntity parent) {
		super(parent);
	}

	@Override
	public Interaction getAllowedInteraction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interaction[] getAllowedInteractions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		AbstractEntity e1 = new EntityTest(),
				e2 = new EntityTest(e1),
				e3 = new EntityTest();
		
		System.out.println(e1.getJSONDescriptor().toString());

		System.out.println(e2.getJSONDescriptor().toString());
		
		System.out.println(e1.toString() + " == " + e2.toString() + "? => " + e1.equals(e2));

		System.out.println(e1.toString() + " == " + e3.toString() + "? => " + e1.equals(e3));
	}

}
