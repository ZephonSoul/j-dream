package com.dream.core.coordination.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.Formula;

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
	public Operation bindEntityReference(EntityInstanceReference componentReference,
			EntityInstanceActual actualComponent) {
		return new IfThenElse(
				condition.bindEntityReference(componentReference, actualComponent),
				thenOperation.bindEntityReference(componentReference, actualComponent),
				elseOperation.bindEntityReference(componentReference, actualComponent)
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
