/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Caching;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class InstanceIdentifier extends AbstractExpression {

	private Instance<Entity> entityInstance;
	
	public InstanceIdentifier(Instance<Entity> entityInstance) {
		this.entityInstance = entityInstance;
	}

	/**
	 * @return the entityInstance
	 */
	public Instance<Entity> getEntityInstance() {
		return entityInstance;
	}

	@Override
	public void evaluateOperands() {	}

	@Override
	public void clearCache() {
		if (entityInstance instanceof Caching)
			((Caching)entityInstance).clearCache();
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof InstanceIdentifier) &&
				((InstanceIdentifier) ex).getEntityInstance().equals(entityInstance);
	}

	@Override
	public boolean allOperandsValued() {
		//TODO: uniform
		return (entityInstance instanceof ActualInstance<?> || entityInstance instanceof EntityInstanceActual);
	}

	@Override
	protected Value computeResult() {
		return new NumberValue(entityInstance.getActual().getId());
	}

	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		Instance<Entity> newInstance = Bindable.bindInstance(entityInstance,reference,actual);
		if (!(newInstance instanceof Bindable))
			return new NumberValue(newInstance.getActual().getId());
		else
			return new InstanceIdentifier(newInstance);
	}

}
