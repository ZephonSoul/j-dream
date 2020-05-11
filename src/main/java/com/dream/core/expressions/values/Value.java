package com.dream.core.expressions.values;

import com.dream.core.expressions.Expression;

/**
 * @author Alessandro Maggi
 *
 */
public interface Value extends Expression {

	public boolean equals(Value value);
	
	public default boolean allOperandsValued() {
		return true;
	}
	
}
