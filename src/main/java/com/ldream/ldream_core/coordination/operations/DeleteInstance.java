package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class DeleteInstance extends AbstractOperation implements Operation {

	public static int BASE_CODE = 2000;
	
	private ComponentInstance parentInstance;
	private ComponentInstance targetInstance;

	public DeleteInstance(
			ComponentInstance parentInstance,
			ComponentInstance targetInstance) {
		
		this.parentInstance = parentInstance;
		this.targetInstance = targetInstance;
	}
	
	/**
	 * @return the parentInstance
	 */
	public ComponentInstance getParentInstance() {
		return parentInstance;
	}

	/**
	 * @return the parentInstance
	 */
	public ComponentInstance getTargetInstance() {
		return targetInstance;
	}

	@Override
	public void evaluateParams() {}

	@Override
	public void execute() {
		parentInstance.getComponent().removeFromPool(targetInstance.getComponent());
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof DeleteInstance)
				&& parentInstance.equals(((DeleteInstance) op).getParentInstance());
	}

	@Override
	public Operation bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (parentInstance.equals(componentReference))
			return new DeleteInstance(
					actualComponent,
					targetInstance);
		else if (targetInstance.equals(componentReference))
			return new DeleteInstance(
					parentInstance,
					actualComponent);
		else
			return this;
	}

	public String toString() {
		return String.format("delete(%s.%s)",
				parentInstance.getName(),
				targetInstance.getName());
	}

}
