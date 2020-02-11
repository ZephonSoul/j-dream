package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.expressions.ActualVariable;
import com.ldream.ldream_core.expressions.VariableExpression;

public class Assign implements Operation {
	
	public static int BASE_CODE = 100;

	private VariableExpression localVariable;
	private Expression valueExpression;
	private Number value;
	
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
	 * @return the value
	 */
	public Number getValue() {
		return value;
	}

	/**
	 * @return the localVariable
	 */
	public VariableExpression getLocalVariable() {
		return localVariable;
	}
	
	@Override
	public void evaluateParams() {
		value = valueExpression.eval();
	}

	@Override
	public void execute() {
		if (localVariable instanceof ActualVariable)
			((ActualVariable)localVariable).getLocalVariable().setValue(value);
		//TODO: raise exception otherwise
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + localVariable.hashCode() + valueExpression.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Assign)
			return equals((Assign) o);
		else
			return false;
	}
	
	public boolean equals(Operation op) {
		boolean test = (op instanceof Assign) && 
				(valueExpression.equals(((Assign)op).getValueExpression())) &&
				(localVariable.equals(((Assign)op).getLocalVariable()));
		return test;
	}
	
	public String toString() {
		return String.format("assign(%s,%s)",
				localVariable.toString(),
				valueExpression.toString());
	}

	@Override
	public Operation bindActualComponent(
			ComponentInstance componentVariable, 
			Component actualComponent) {
		
		return new Assign(
				(VariableExpression)localVariable.bindActualComponent(componentVariable, actualComponent),
				valueExpression.bindActualComponent(componentVariable, actualComponent));
	}

}
