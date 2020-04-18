/**
 * 
 */
package com.dream.core.coordination.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.entities.behavior.Transition;

/**
 * @author Alessandro Maggi
 *
 */
public class TriggerTransition extends AbstractOperation implements Operation {
	
	private Transition transition;
	/**
	 * 
	 */
	public TriggerTransition(Transition transition) {
		this.transition = transition;
	}
	
	public Transition getTransition() {
		return transition;
	}

	@Override
	public Operation bindEntityReference(EntityInstanceReference entityReference, EntityInstanceActual entityActual) {
		return this;
	}

	@Override
	public void clearCache() {}

	@Override
	public void evaluateOperands() {	}

	@Override
	public void execute() {
		transition.trigger();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof TriggerTransition) &&
				transition.equals(((TriggerTransition)op).getTransition());
	}
	
	@Override
	public String toString() {
		return String.format("trigger%s", transition.toString());
	}

}
