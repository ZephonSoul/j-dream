/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public class MapPropertyRef<T> implements Instance<T>, Bindable<Instance<T>> {

	private Instance<Entity> scope;
	private String propertyName;

	public MapPropertyRef(
			Instance<Entity> scope,
			String propertyName) {

		this.scope = scope;
		this.propertyName = propertyName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getActual() {
		try {
			return (T) ((AbstractMotif) scope.getActual()).getMapProperty(propertyName).get();
		} catch (UnboundReferenceException ex) {
			return null;
		}
	}

	@Override
	public <I> Instance<T> bindInstance(Instance<I> reference, Instance<I> actual) {
		MapPropertyRef<T> newInstance = new MapPropertyRef<>(
				Bindable.bindInstance(scope, reference, actual),
				propertyName);
		T value = newInstance.getActual();
		if (value == null)
			return newInstance;
		else
			return new ActualInstance<T>(value);
	}

	public String toString() {
		return String.format("%s(%s)", propertyName, scope.toString());
	}

}
