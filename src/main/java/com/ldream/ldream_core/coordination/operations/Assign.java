package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.expressions.ActualVariable;
import com.ldream.ldream_core.expressions.VariableExpression;

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
	public Operation bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Assign(
				(VariableExpression) localVariable.bindActualComponent(
						componentVariable, actualComponent),
				valueExpression.bindActualComponent(
						componentVariable, actualComponent));
	}

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
