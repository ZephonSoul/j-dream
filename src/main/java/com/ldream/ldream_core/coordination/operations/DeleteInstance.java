package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.components.OrphanComponentException;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class DeleteInstance extends AbstractOperation implements Operation {

	final static int BASE_CODE = 10000;

	private ComponentInstance targetInstance;

	public DeleteInstance(ComponentInstance targetInstance) {
		this.targetInstance = targetInstance;
	}

	private ComponentInstance getTargetInstance() {
		return targetInstance;
	}

	@Override
	public void evaluateParams() {}

	@Override
	public void execute() {
		try {
			targetInstance.getComponent().getParent().removeFromPool(
					targetInstance.getComponent());
		} catch (OrphanComponentException e) {
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
	public Operation bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (targetInstance.equals(componentReference))
			return new DeleteInstance(actualComponent);
		else
			return this;
	}

	public String toString() {
		return String.format("delete(%s)",
				targetInstance.getName());
	}

}
