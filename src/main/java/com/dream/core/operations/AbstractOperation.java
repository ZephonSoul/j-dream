package com.dream.core.operations;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractOperation implements Operation {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Operation)
				&& this.equals((Operation) o);
	}
	
	public boolean lowLevelEquals(Object o) {
		return super.equals(o);
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> componentReference, 
			Instance<I> actualComponent) {
		return this;
	}
	
}
