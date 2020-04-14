package com.dream.core.expressions;

import com.dream.core.Bindable;
import com.dream.core.expressions.values.Value;

public interface Expression extends Bindable<Expression> {

	public Value eval();
	
	public void evaluateOperands();
	
	public void clearCache();
	
	public boolean equals(Expression ex);
}
