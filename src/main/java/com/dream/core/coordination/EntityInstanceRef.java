package com.dream.core.coordination;

import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class EntityInstanceRef 
implements EntityInstance, Bindable<EntityInstance> {

	final static int BASE_CODE = 111;

	private String name;
	
	public EntityInstanceRef() {
		this.name = EntityRefNamesFactory.getInstance().getFreshName();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + name.hashCode();
	}
	
	public boolean equals(EntityInstanceRef instanceReference) {
		return name.equals(instanceReference.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof EntityInstanceRef)
				&& equals((EntityInstanceRef) o);
	}

	@Override
	public <I> EntityInstance bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		if (this.equals(reference)) {
			return (EntityInstance) actual;
		} else
			return this;
	}
	
	public String toString() {
		return this.name;
	}

	@Override
	public Entity getActual() {
		throw new UnboundReferenceException(this);
	}
	
}
