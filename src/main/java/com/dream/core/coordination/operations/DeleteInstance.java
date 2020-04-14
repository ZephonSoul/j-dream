package com.dream.core.coordination.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.OrphanEntityException;
import com.dream.core.entities.CoordinatingEntity;

public class DeleteInstance extends AbstractOperation implements Operation {

	final static int BASE_CODE = 10000;

	private EntityInstance targetInstance;

	public DeleteInstance(EntityInstance targetInstance) {
		this.targetInstance = targetInstance;
	}

	private EntityInstance getTargetInstance() {
		return targetInstance;
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
	}

	@Override
	public int hashCode() {
		return BASE_CODE + targetInstance.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof DeleteInstance)
				&& targetInstance.equals(((DeleteInstance) op).getTargetInstance());
	}

	@Override
	public Operation bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		if (targetInstance.equals(entityReference))
			return new DeleteInstance(entityActual);
		else
			return this;
	}

	@Override
	public void clearCache() {}

	public String toString() {
		return String.format("delete(%s)",
				targetInstance.getName());
	}

}
