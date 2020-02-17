package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.UnboundReferenceException;
import com.ldream.ldream_core.values.Value;

public class ReferencedVariable extends AbstractExpression implements VariableExpression {

	private ComponentInstance componentInstance;
	private String localVariableName;

	public ReferencedVariable(
			ComponentInstance componentInstance, 
			String localVariableName) {

		this.componentInstance = componentInstance;
		this.localVariableName = localVariableName;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof ReferencedVariable) 
				&& (componentInstance.equals(((ReferencedVariable) ex).getComponentInstance()))
				&& (localVariableName.equals(((ReferencedVariable) ex).getVariableName()));
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (this.componentInstance.equals(componentReference))
			return new ActualVariable(actualComponent.getComponent().getLocalVariable(localVariableName));
		else
			return this;
	}

	/**
	 * @return the componentVariable
	 */
	public ComponentInstance getComponentInstance() {
		return componentInstance;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return localVariableName;
	}

	@Override
	public String toString() {
		return String.format("%s.%s",componentInstance.getName(),localVariableName);
	}

	@Override
	public int hashCode() {
		return componentInstance.hashCode() + localVariableName.hashCode();
	}

	@Override
	public void evaluateOperands() {	}

	@Override
	public Value computeResult() {
		throw new UnboundReferenceException(componentInstance);
	}

	@Override
	public boolean allOperandsValued() {
		return false;
	}

	@Override
	public void clearCache() {}

}
