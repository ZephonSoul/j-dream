/**
 * 
 */
package com.dream.core.coordination.constraints;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.UnboundReferenceException;

/**
 * @author Alessandro Maggi
 *
 */
public class Idle extends AbstractFormula implements Formula {
	
	private Instance<Entity> entity;

	/**
	 * 
	 */
	public Idle(Instance<Entity> entity) {
		this.entity = entity;
	}
	
	public Instance<Entity> getEntity() {
		return entity;
	}

//	@Override
//	public Formula bindEntityReference(EntityInstanceRef entityReference, EntityInstanceActual entityActual) {
//		if (entity.equals(entityReference))
//			return new Idle(entityActual);
//		else
//			return this;
//	}

	@Override
	public boolean sat(Interaction i) {
		if (entity instanceof EntityInstanceActual)
			return !i.involvesEntity((EntityInstanceActual)entity);
		else
			throw new UnboundReferenceException(entity);
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Idle) &&
				entity.equals(((Idle)formula).getEntity());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		if (entity.equals(reference))
			return new Idle((Instance<Entity>) actual);
		else
			return this;
	}

}
