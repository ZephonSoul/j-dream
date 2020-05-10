package com.dream.core.operations;

import com.dream.core.Bindable;
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
			Instance<I> reference, 
			Instance<I> actual) {
		return this;
	}
	

	@SuppressWarnings("unchecked")
	protected <I,T> T bindInstance(
			T scope,
			Instance<I> reference,
			Instance<I> actual) {
		
		T newScope;
		if (scope instanceof Bindable<?>)
			newScope = ((Bindable<T>) scope).bindInstance(reference,actual);
		else
			newScope = scope;
		
		return newScope;
		
	}
	
}
