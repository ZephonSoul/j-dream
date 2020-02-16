package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class ReferencedVariable implements VariableExpression {
	
	private ComponentInstance componentInstance;
	private String localVariableName;
	
	public ReferencedVariable(ComponentInstance componentInstance, String localVariableName) {
		this.componentInstance = componentInstance;
		this.localVariableName = localVariableName;
	}

	@Override
	public Number eval() {
		// TODO raise exception
		return null;
	}

	@Override
	public boolean equals(Expression ex) {
		if (ex instanceof ReferencedVariable) {
			return (componentInstance.equals(((ReferencedVariable) ex).getComponentInstance()))
					&&
					(localVariableName.equals(((ReferencedVariable) ex).getVariableName()));
		} else
			return false;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if (o instanceof ReferencedVariable)
//			return equals((ReferencedVariable)o);
//		else
//			return false;
//	}

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

}
