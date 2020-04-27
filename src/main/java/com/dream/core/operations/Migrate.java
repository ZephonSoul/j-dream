package com.dream.core.operations;

import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.Instance;
import com.dream.core.OrphanEntityException;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.entities.CoordinatingEntity;

public class Migrate extends AbstractOperation {

	final static int BASE_CODE = 5000;

	protected EntityInstance entity;
	protected EntityInstance targetParent;

	public Migrate(
			EntityInstance entity,
			EntityInstance targetParent) {

		this.entity = entity;
		this.targetParent = targetParent;
	}

	/**
	 * @return the cInstance
	 */
	public EntityInstance getEntity() {
		return entity;
	}

	/**
	 * @return the newParent
	 */
	public EntityInstance getTargetParent() {
		return targetParent;
	}

	@Override
	public void evaluateOperands() {}

	@Override
	public void execute() {
		try {
			CoordinatingEntity parentEntity = (CoordinatingEntity) entity.getActual().getParent();
			if (parentEntity instanceof CoordinatingEntity)
				parentEntity.removeFromPool(entity.getActual());
			else
				throw new IllegalScopeException(parentEntity, this.toString());
		} catch (OrphanEntityException e) {
			//Best-effort delete (if component already orphan, do nothing)
			//TODO: link logger to log event
		}
		CoordinatingEntity targetParentEntity = (CoordinatingEntity) targetParent.getActual();
		if (targetParentEntity instanceof CoordinatingEntity)
			targetParentEntity.addToPool(entity.getActual());
		else 
			throw new IncompatibleEntityReference(targetParent, this.toString());
	}

	@Override
	public int hashCode() {
		return BASE_CODE
				+ entity.hashCode()
				+ targetParent.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof Migrate)
				&& entity.equals(((Migrate) op).getEntity())
				&& targetParent.equals(((Migrate) op).getTargetParent());
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (entity.equals(reference))
			return new Migrate(
					(EntityInstance) actual,
					targetParent);
		else if (targetParent.equals(reference))
			return new Migrate(
					entity,
					(EntityInstance) actual);
		else
			return this;
	}
	
	@Override
	public void clearCache() {}

	public String toString() {
		return String.format("migrate(%s,%s)",
				entity.getName(),
				targetParent.getName());
	}

}
