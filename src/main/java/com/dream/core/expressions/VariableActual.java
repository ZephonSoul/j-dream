package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.expressions.values.Value;
import com.dream.core.localstore.LocalVariable;

/**
 * @author Alessandro Maggi
 *
 */
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
		evaluateOperands();
		return variableValue;
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
		return eval().toString();
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

	public String getVarName() {
		return localVariable.getInstanceName();
	}

	@Override
	public LocalVariable getActual() {
		return localVariable;
	}

}
