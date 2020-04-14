package com.dream.core.expressions;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.entities.LocalVariable;
import com.dream.core.expressions.values.Value;

public class ActualVariable extends AbstractExpression implements VariableExpression {
	
	private LocalVariable localVariable;
	private Value variableValue;
	
	public ActualVariable(LocalVariable localVariable) {
		this.localVariable = localVariable;
	}
	
	public LocalVariable getLocalVariable() {
		return localVariable;
	}

	@Override
	public Value eval() {
		return localVariable.getValue();
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex.getClass().equals(ActualVariable.class)) &&
				(localVariable.equals(((ActualVariable)ex).getLocalVariable()));
	}
	
	@Override
	public int hashCode() {
		return localVariable.hashCode();
	}

	@Override
	public Expression bindEntityReference(
			EntityInstanceReference componentReference, 
			EntityInstanceActual actualComponent) {
		
		return this;
	}
	
	@Override
	public String toString() {
		return localVariable.getInstanceName();
	}

	@Override
	public void evaluateOperands() {
		if (variableValue == null)
			variableValue = localVariable.getValue();
	}

	@Override
	public Value computeResult() {
		return variableValue;
	}

	@Override
	public boolean allOperandsValued() {
		return (variableValue != null);
	}

	@Override
	public void clearCache() {
		variableValue = null;
	}

}
