package com.dream.core.coordination;

import com.dream.core.Entity;

public class EntityInstanceReference implements EntityInstance {

	final static int BASE_CODE = 111;

	private String name;
	
	public EntityInstanceReference() {
		this.name = EntityVariableNamesFactory.getInstance().getFreshName();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + name.hashCode();
	}
	
	public boolean equals(EntityInstanceReference instanceReference) {
		return name.equals(instanceReference.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof EntityInstanceReference)
				&& equals((EntityInstanceReference) o);
	}

	@Override
	public EntityInstance bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {
		
		if (this.equals(entityReference)) {
			return entityActual;
		} else
			return this;
	}
	
	public String toString() {
		return this.name;
	}

	@Override
	public Entity getActualEntity() {
		throw new UnboundReferenceException(this);
	}
	
}
