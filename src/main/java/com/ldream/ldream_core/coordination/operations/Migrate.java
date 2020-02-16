package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.components.OrphanComponentException;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class Migrate extends AbstractOperation implements Operation {

	public static int BASE_CODE = 10000;

	private ComponentInstance targetInstance;
	private ComponentInstance newParent;

	public Migrate(
			ComponentInstance targetInstance,
			ComponentInstance newParent) {

		this.targetInstance = targetInstance;
		this.newParent = newParent;
	}

	/**
	 * @return the cInstance
	 */
	public ComponentInstance getTargetInstance() {
		return targetInstance;
	}

	/**
	 * @return the newParent
	 */
	public ComponentInstance getNewParent() {
		return newParent;
	}

	@Override
	public void evaluateParams() {}

	@Override
	public void execute() {
		try {
			targetInstance.getComponent().getParent().removeFromPool(targetInstance.getComponent());
			newParent.getComponent().addToPool(targetInstance.getComponent());
		} catch (OrphanComponentException e) {
			//Best-effort delete (if component already orphan, do nothing)
			//TODO: link logger to log event
		}
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof Migrate)
				&& targetInstance.equals(((Migrate) op).getTargetInstance())
				&& newParent.equals(((Migrate) op).getNewParent());
	}

	@Override
	public Operation bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (targetInstance.equals(componentReference))
			return new Migrate(
					actualComponent,
					newParent);
		else if (newParent.equals(componentReference))
			return new Migrate(
					targetInstance,
					actualComponent);
		else
			return this;
	}

	public String toString() {
		return String.format("migrate(%s,%s)",
				targetInstance.getName(),
				newParent.getName());
	}

}
