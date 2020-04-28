package com.dream.core.expressions.values;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractValue implements Value {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Value);
	}
	
	@Override
	public <I> Value bindInstance(Instance<I> reference, Instance<I> actual) {
		return this;
	}

}
