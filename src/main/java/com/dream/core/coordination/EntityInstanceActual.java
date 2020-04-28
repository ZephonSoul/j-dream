package com.dream.core.coordination;

import com.dream.core.Entity;
import com.dream.core.Instance;

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
	public String getName() {
		return entity.toString();
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

	@Override
	public <I> EntityInstance bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return this;
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public Entity getActual() {
		return entity;
	}

//	@Override
//	public <I> EntityInstance bindInstance(
//			Instance<I> reference, 
//			Instance<I> actual) {
//		
//		return this;
//	}

}
