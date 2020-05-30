package com.dream.core.coordination;

import com.dream.core.Entity;

/**
 * @author Alessandro Maggi
 *
 */
public class EntityInstanceActual 
implements EntityInstance {

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
	public int hashCode() {
		return BASE_CODE + entity.hashCode();
	}

	public boolean equals(EntityInstanceActual instance) {
		return entity.equals(instance.getActual());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof EntityInstanceActual)
				&& equals((EntityInstanceActual) o);
	}
	
	public String toString() {
		return entity.toString();
	}

	@Override
	public Entity getActual() {
		return entity;
	}

	@Override
	public void evaluate() {}

}
