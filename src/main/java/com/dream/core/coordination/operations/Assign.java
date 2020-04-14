package com.dream.core.coordination.operations;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.expressions.ActualVariable;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.VariableExpression;

public class Assign extends AbstractOperation implements Operation {

	final static int BASE_CODE = 100;

	private VariableExpression localVariable;
	private Expression valueExpression;

	public Assign(VariableExpression localVariable, Expression valueExpression) {
		this.localVariable = localVariable;
		this.valueExpression = valueExpression;
	}

	/**
	 * @return the valueExpression
	 */
	public Expression getValueExpression() {
		return valueExpression;
	}

	/**
	 * @return the localVariable
	 */
	public VariableExpression getLocalVariable() {
		return localVariable;
	}

	@Override
	public void evaluateOperands() {
		valueExpression.evaluateOperands();
	}

	@Override
	public void execute() {
		if (localVariable instanceof ActualVariable)
			((ActualVariable)localVariable).getLocalVariable().setValue(valueExpression.eval());
		//TODO: raise exception otherwise
	}

	@Override
	public int hashCode() {
		return BASE_CODE + localVariable.hashCode() + valueExpression.hashCode();
	}

	@Override
	public Operation bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		return new Assign(
				(VariableExpression) localVariable.bindEntityReference(
						entityReference, entityActual),
				valueExpression.bindEntityReference(
						entityReference, entityActual));
	}

	@Override
	public void clearCache() {
		valueExpression.clearCache();
	}

	public boolean equals(Operation op) {
		return (op instanceof Assign) 
				&& (valueExpression.equals(((Assign)op).getValueExpression())) 
				&& (localVariable.equals(((Assign)op).getLocalVariable()));
	}

	public String toString() {
		return String.format("assign(%s,%s)",
				localVariable.toString(),
				valueExpression.toString());
	}

}
