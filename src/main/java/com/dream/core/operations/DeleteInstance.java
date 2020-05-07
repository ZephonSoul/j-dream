package com.dream.core.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.Instance;
import com.dream.core.OrphanEntityException;
import com.dream.core.entities.CoordinatingEntity;

/**
 * @author Alessandro Maggi
 *
 */
public class DeleteInstance extends AbstractOperation {

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
			CoordinatingEntity parent = 
					(CoordinatingEntity) targetInstance.getActual().getParent();
			if (parent instanceof CoordinatingEntity)
				parent.removeFromPool(targetInstance.getActual());
			else
				throw new IllegalScopeException(parent, this.toString());
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
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (targetInstance instanceof EntityInstanceActual || !targetInstance.equals(reference))
			return this;
		else
			return new DeleteInstance((EntityInstance) actual);
	}

	@Override
	public void clearCache() {}

	public String toString() {
		return String.format("delete(%s)",
				targetInstance.toString());
	}

}
