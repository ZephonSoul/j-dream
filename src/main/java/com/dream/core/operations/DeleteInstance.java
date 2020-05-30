package com.dream.core.operations;

import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.OrphanEntityException;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.maps.MappingNotFoundException;

/**
 * @author Alessandro Maggi
 *
 */
public class DeleteInstance extends AbstractOperation {

	final static int BASE_CODE = 10000;

	private Instance<Entity> targetInstance;

	public DeleteInstance(Instance<Entity> targetInstance) {
		this.targetInstance = targetInstance;
	}

	private Instance<Entity> getTargetInstance() {
		return targetInstance;
	}

	@Override
	public void evaluate() {
		targetInstance.evaluate();
	}

	@Override
	public void execute() {
		try {
			CoordinatingEntity parent = 
					(CoordinatingEntity) targetInstance.getActual().getParent();
			if (parent instanceof CoordinatingEntity)
				try {
					parent.removeFromPool(targetInstance.getActual());
				} catch (MappingNotFoundException ex) {
					//Multiple deletes may leave the entity already unmapped
				}
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

//		if (targetInstance instanceof EntityInstanceActual || !targetInstance.equals(reference))
//			return this;
//		else
//			return new DeleteInstance((EntityInstance) actual);
		return new DeleteInstance(bindInstance(targetInstance,reference,actual));
	}

	@Override
	public void clearCache() {}

	public String toString() {
		return String.format("delete(%s)",
				targetInstance.toString());
	}

}
