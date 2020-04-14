package com.dream.core.coordination;

import com.dream.core.Entity;

public class EntityInstanceActual implements EntityInstance {

	public static int BASE_CODE = 555;

	private Entity entity;

	public EntityInstanceActual(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @param component the componentInstance to set
	 */
	public void setActualComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String getName() {
		return entity.toString();
	}

	@Override
	public int hashCode() {
		return BASE_CODE + entity.hashCode();
	}

	public boolean equals(EntityInstanceActual instance) {
		return entity.equals(instance.getActualEntity());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof EntityInstanceActual)
				&& equals((EntityInstanceActual) o);
	}

	@Override
	public EntityInstance bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		return this;
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public Entity getActualEntity() {
		return entity;
	}

}
