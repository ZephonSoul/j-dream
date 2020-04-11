package com.dream.core.expressions;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.expressions.values.Value;

/**
 * @deprecated
 * Constant expressions are no longer necessary as Value instances
 * directly extend Expressions and can be used as constants
 */
@Deprecated
public class Constant implements Expression {

	private Value value;
	
	public Constant(Value value) {
		this.value = value;
	}
	
	public Value getValue() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Constant) && equals((Constant) o);
	}
	
	@Override
	public boolean equals(Expression ex) {
		return (ex.getClass().equals(Constant.class) 
				&& value.equals(((Constant)ex).getValue()));
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {
		
		return this;
	}

	@Override
	public Value eval() {
		return value;
	}

	@Override
	public void evaluateOperands() {}
	
	@Override
	public void clearCache() {}

}
