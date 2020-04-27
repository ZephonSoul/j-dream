/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.entities.behavior.Transition;

/**
 * @author Alessandro Maggi
 *
 */
public class TriggerTransition extends AbstractOperation {
	
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
