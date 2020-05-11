package com.dream.core.expressions;

import com.dream.core.Bindable;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public interface Expression extends Bindable<Expression> {

	public Value eval();
	
	public void evaluateOperands();
	
	public void clearCache();
	
	public boolean equals(Expression ex);
	
	public boolean allOperandsValued();
}
