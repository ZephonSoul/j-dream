package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.entities.LocalVariable;
import com.dream.core.expressions.values.Value;

public class VariableActual 
extends AbstractExpression implements Instance<LocalVariable> {
	
	private LocalVariable localVariable;
	private Value variableValue;
	
	public VariableActual(LocalVariable localVariable) {
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
		return (ex.getClass().equals(VariableActual.class)) &&
				(localVariable.equals(((VariableActual)ex).getLocalVariable()));
	}
	
	@Override
	public int hashCode() {
		return localVariable.hashCode();
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

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public LocalVariable getActual() {
		return localVariable;
	}

}
