package com.dream.core.coordination.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.OrphanEntityException;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.entities.CoordinatingEntity;

public class Migrate extends AbstractOperation implements Operation {

	final static int BASE_CODE = 5000;

	private EntityInstance targetInstance;
	private EntityInstance newParent;

	public Migrate(
			EntityInstance targetInstance,
			EntityInstance newParent) {

		this.targetInstance = targetInstance;
		this.newParent = newParent;
	}

	/**
	 * @return the cInstance
	 */
	public EntityInstance getTargetInstance() {
		return targetInstance;
	}

	/**
	 * @return the newParent
	 */
	public EntityInstance getNewParent() {
		return newParent;
	}

	@Override
	public void evaluateOperands() {}

	@Override
	public void execute() {
		try {
			if (targetInstance.getActualEntity().getParent() instanceof CoordinatingEntity)
				((CoordinatingEntity)targetInstance.getActualEntity().getParent()).removeFromPool(
						targetInstance.getActualEntity());
			else
				throw new IncompatibleEntityReference(targetInstance.getActualEntity(), this.toString());
		} catch (OrphanEntityException e) {
			//Best-effort delete (if component already orphan, do nothing)
			//TODO: link logger to log event
		}
		if (newParent.getActualEntity() instanceof CoordinatingEntity)
			((CoordinatingEntity)newParent.getActualEntity()).addToPool(targetInstance.getActualEntity());
		else throw new IncompatibleEntityReference(newParent.getActualEntity(), this.toString());
	}

	@Override
	public int hashCode() {
		return BASE_CODE
				+ targetInstance.hashCode()
				+ newParent.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof Migrate)
				&& targetInstance.equals(((Migrate) op).getTargetInstance())
				&& newParent.equals(((Migrate) op).getNewParent());
	}

	@Override
	public Operation bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		if (targetInstance.equals(entityReference))
			return new Migrate(
					entityActual,
					newParent);
		else if (newParent.equals(entityReference))
			return new Migrate(
					targetInstance,
					entityActual);
		else
			return this;
	}
	
	@Override
	public void clearCache() {}

	public String toString() {
		return String.format("migrate(%s,%s)",
				targetInstance.getName(),
				newParent.getName());
	}

}
