package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;

public interface Expression {

	public Number eval();
	
	public boolean equals(Expression ex);
	
	public Expression instantiateComponentVariable(
			ComponentVariable componentVariable, 
			Component actualComponent);
}
