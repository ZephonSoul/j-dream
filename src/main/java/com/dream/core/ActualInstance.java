/**
 * 
 */
package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
public class ActualInstance<T> implements Instance<T> {

	private T actual;
	
	public ActualInstance(T actual) {
		this.actual = actual;
	}

	@Override
	public T getActual() {
		return actual;
	}
	
	public String toString() {
		return actual.toString();
	}

	@Override
	public void evaluate() {}

}
