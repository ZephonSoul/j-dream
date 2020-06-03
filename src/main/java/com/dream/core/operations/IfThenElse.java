package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.expressions.EvaluationRuntimeException;

/**
 * @author Alessandro Maggi
 *
 */
public class IfThenElse extends AbstractOperation {

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
	public <I> Operation bindInstance(
			Instance<I> reference,
			Instance<I> actual) {

		return new IfThenElse(
				condition.bindInstance(reference, actual),
				thenOperation.bindInstance(reference, actual),
				elseOperation.bindInstance(reference, actual)
				);
	}

	@Override
	public void evaluate() {
		condition.evaluateExpressions();
		thenOperation.evaluate();
		elseOperation.evaluate();
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
		try {
			if (condition.sat())
				return op.equals(thenOperation);
			else
				return op.equals(elseOperation);
		} catch (UnboundReferenceException | EvaluationRuntimeException ex) {
			return (op instanceof IfThenElse)
					&& condition.equals(((IfThenElse) op).getCondition())
					&& thenOperation.equals(((IfThenElse) op).getThenOperation())
					&& elseOperation.equals(((IfThenElse) op).getElseOperation());
		}
	}

	@Override
	public void clearCache() {
		condition.clearCache();
		thenOperation.clearCache();
		elseOperation.clearCache();
	}

	public String toString() {
//		try {
//			if (condition.sat())
//				return thenOperation.toString();
//			else
//				return elseOperation.toString();
//		} catch (UnboundReferenceException | EvaluationRuntimeException ex) {
			String elseBranch = "";
			if (!(elseOperation instanceof Skip))
				elseBranch = String.format(" ELSE [%s]", elseOperation.toString());
			return String.format("IF (%s) THEN [%s]%s",
					condition.toString(),
					thenOperation.toString(),
					elseBranch);
//		}
	}

}
