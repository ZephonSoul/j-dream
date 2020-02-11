package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.Bindable;

public interface Expression extends Bindable<Expression> {

	public Number eval();
	
	public boolean equals(Expression ex);
}
