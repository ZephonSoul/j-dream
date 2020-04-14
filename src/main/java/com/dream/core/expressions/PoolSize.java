package com.dream.core.expressions;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

public class PoolSize extends AbstractExpression implements Expression {

	public static final int BASE_CODE = 31;

	private EntityInstance entityInstance;
	private TypeRestriction type;
	private Value value;

	/**
	 * @param entityInstance
	 * @param type
	 * @param value
	 */
	public PoolSize(
			EntityInstance entityInstance, 
			TypeRestriction type, 
			Value value) {
		this.entityInstance = entityInstance;
		this.type = type;
		this.value = value;
	}

	/**
	 * @param componentInstance
	 * @param type
	 */
	public PoolSize(EntityInstance componentInstance, TypeRestriction type) {
		this(componentInstance,type,null);
	}

	public PoolSize(EntityInstance componentInstance) {
		this(componentInstance,TypeRestriction.anyType());
	}

	public EntityInstance getComponentInstance() {
		return entityInstance;
	}

	public TypeRestriction getTypeRestriction() {
		return type;
	}

	@Override
	public Expression bindEntityReference(
			EntityInstanceReference entityReference,
			EntityInstanceActual entityActual) {

		if (this.entityInstance.equals(entityReference))
			return new PoolSize(
					entityActual,
					type,
					value);
		else
			return this;
	}

	@Override
	public void evaluateOperands() {
		if (value == null)
			if (entityInstance instanceof EntityInstanceActual) {
				if (entityInstance.getActualEntity() instanceof CoordinatingEntity) {
					int size = ((CoordinatingEntity) entityInstance.getActualEntity()).getPoolSize();
					value = new NumberValue(size);
				} else
					throw new IncompatibleEntityReference(entityInstance.getActualEntity(),this.toString());
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
		return (ex instanceof PoolSize)
				&& entityInstance.equals(((PoolSize) ex).getComponentInstance())
				&& type.equals(((PoolSize) ex).getTypeRestriction());
	}

	public String toString() {
		return String.format("size(%s.C:%s)",
				entityInstance.getName(),
				type.toString()
				);
	}

	@Override
	public void clearCache() {
		value = null;
	}

	@Override
	public int hashCode() {
		return BASE_CODE + entityInstance.hashCode() + type.hashCode();
	}

}
