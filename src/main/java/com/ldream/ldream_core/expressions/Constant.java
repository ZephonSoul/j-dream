package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class Constant implements Expression {

	private Number value;
	
	public Constant(Number value) {
		this.value = value;
	}
	
	public Number getValue() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	@Override
	public int hashCode() {
		return value.intValue();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Constant)
			return equals((Constant) o);
		else
			return false;
	}
	
	@Override
	public boolean equals(Expression ex) {
		return (ex.getClass().equals(Constant.class) && 
				value.equals(((Constant)ex).getValue()));
	}

	@Override
	public Number eval() {
		return value;
	}

	@Override
	public Expression bindActualComponent(ReferencedComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}

}
