package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;

public class ReferencedVariable implements VariableExpression {
	
	private ComponentVariable componentVariable;
	private String variableName;
	
	public ReferencedVariable(ComponentVariable componentVariable, String variableName) {
		this.componentVariable = componentVariable;
		this.variableName = variableName;
	}

	@Override
	public Number eval() {
		// TODO raise exception
		return null;
	}

	@Override
	public boolean equals(Expression ex) {
		if (ex instanceof ReferencedVariable) {
			return (componentVariable.equals(((ReferencedVariable) ex).getComponentVariable()))
					&&
					(variableName.equals(((ReferencedVariable) ex).getComponentVariable()));
		} else
			return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ReferencedVariable)
			return equals((ReferencedVariable)o);
		else
			return false;
	}

	@Override
	public Expression instantiateComponentVariable(
			ComponentVariable componentVariable, 
			Component actualComponent) {
		
		if (this.componentVariable.equals(componentVariable))
			return new ActualVariable(actualComponent.getLocalVariable(variableName));
		else
			return this;
	}

	/**
	 * @return the componentVariable
	 */
	public ComponentVariable getComponentVariable() {
		return componentVariable;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s",componentVariable.getName(),variableName);
	}
	
	@Override
	public int hashCode() {
		return componentVariable.hashCode() + variableName.hashCode();
	}

}
