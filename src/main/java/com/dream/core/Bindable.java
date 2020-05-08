package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
public interface Bindable<T> {

	public <I> T bindInstance(
			Instance<I> reference, 
			Instance<I> actual);

	@SuppressWarnings("unchecked")
	public static <I,T> T bindInstance(
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
