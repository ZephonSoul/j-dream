package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.Value;

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
	public Expression bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
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
