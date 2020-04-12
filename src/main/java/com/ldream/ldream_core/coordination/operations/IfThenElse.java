package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.constraints.Formula;

public class IfThenElse extends AbstractOperation implements Operation {

	private Formula condition;
	private Operation thenOperation;
	private Operation elseOperation;

	public IfThenElse(
			Formula condition, 
			Operation thenOperation,
			Operation elseOperation) {

		this.condition = condition;
		this.thenOperation = thenOperation;
		this.elseOperation = elseOperation;
	}

	public IfThenElse(
			Formula condition,
			Operation thenOperation) {

		this(condition,thenOperation,Skip.getInstance());
	}


	/**
	 * @return the condition
	 */
	public Formula getCondition() {
		return condition;
	}

	/**
	 * @return the thenOperation
	 */
	public Operation getThenOperation() {
		return thenOperation;
	}

	/**
	 * @return the elseOperation
	 */
	public Operation getElseOperation() {
		return elseOperation;
	}

	@Override
	public Operation bindActualComponent(ComponentInstance componentReference,
			ActualComponentInstance actualComponent) {
		return new IfThenElse(
				condition.bindActualComponent(componentReference, actualComponent),
				thenOperation.bindActualComponent(componentReference, actualComponent),
				elseOperation.bindActualComponent(componentReference, actualComponent)
				);
	}

	@Override
	public void evaluateOperands() {
		thenOperation.evaluateOperands();
		elseOperation.evaluateOperands();
	}

	@Override
	public void execute() {
		if (condition.sat())
			thenOperation.execute();
		else
			elseOperation.execute();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof IfThenElse)
				&& condition.equals(((IfThenElse) op).getCondition())
				&& thenOperation.equals(((IfThenElse) op).getThenOperation())
				&& elseOperation.equals(((IfThenElse) op).getElseOperation());
	}

	@Override
	public void clearCache() {
		condition.clearCache();
		thenOperation.clearCache();
		elseOperation.clearCache();
	}

}
