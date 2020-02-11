package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class ActualVariable implements VariableExpression {
	
	private LocalVariable localVariable;
	
	public ActualVariable(LocalVariable localVariable) {
		this.localVariable = localVariable;
	}
	
	public LocalVariable getLocalVariable() {
		return localVariable;
	}

	@Override
	public Number eval() {
		return localVariable.getValue();
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex.getClass().equals(ActualVariable.class)) &&
				(localVariable.equals(((ActualVariable)ex).getLocalVariable()));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ActualVariable)
			return equals((ActualVariable)o);
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return localVariable.hashCode();
	}

	@Override
	public Expression bindActualComponent(
			ReferencedComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		return this;
	}
	
	@Override
	public String toString() {
		return localVariable.getInstanceName();
	}

}
