package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.values.Value;

public interface Expression extends Bindable<Expression> {

	public Value eval();
	
	public void evaluateOperands();
	
	public void clearCache();
	
	public boolean equals(Expression ex);
}
