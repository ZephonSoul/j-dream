/**
 * 
 */
package com.dream.core.coordination.constraints;

import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.UnboundReferenceException;

/**
 * @author alessandro
 *
 */
public class Idle extends AbstractFormula implements Formula {
	
	private EntityInstance entity;

	/**
	 * 
	 */
	public Idle(EntityInstance entity) {
		this.entity = entity;
	}
	
	public EntityInstance getEntity() {
		return entity;
	}

	@Override
	public Formula bindEntityReference(EntityInstanceReference entityReference, EntityInstanceActual entityActual) {
		if (entity.equals(entityReference))
			return new Idle(entityActual);
		else
			return this;
	}

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

}
