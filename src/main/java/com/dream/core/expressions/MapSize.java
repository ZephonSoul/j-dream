package com.dream.core.expressions;

import com.dream.core.Bindable;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class MapSize extends AbstractExpression {

	public static final int BASE_CODE = 565;

	private EntityInstance entityInstance;
	private Value value;

	/**
	 * @param entityInstance
	 */
	public MapSize(EntityInstance entityInstance) {

		this.entityInstance = entityInstance;
	}

	public EntityInstance getEntityInstance() {
		return entityInstance;
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference,
			Instance<I> actual) {

		if (entityInstance instanceof EntityInstanceActual)
			return this;
		else
			return new MapSize(Bindable.bindInstance(entityInstance,reference,actual));
	}

	@Override
	public void evaluateOperands() {
		if (value == null) 
			if (entityInstance instanceof EntityInstanceActual) {
				AbstractMotif motif = (AbstractMotif) entityInstance.getActual();
				if (motif instanceof AbstractMotif) {
					value = new NumberValue(motif.getMap().getNodesSize());
				} else
					throw new IncompatibleEntityReference(entityInstance,this.toString());
			}
			else
				throw new UnboundReferenceException(entityInstance);
	}

	@Override
	public boolean allOperandsValued() {
		return value != null;
	}

	@Override
	public Value computeResult() {
		return value;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof MapSize)
				&& entityInstance.equals(((MapSize) ex).getEntityInstance());
	}

	public String toString() {
		return String.format("mapSize(%s)",
				entityInstance.toString()
				);
	}

	@Override
	public void clearCache() {
		value = null;
	}

	@Override
	public int hashCode() {
		return BASE_CODE + entityInstance.hashCode();
	}

}
