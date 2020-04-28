package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class PoolSize extends AbstractExpression {

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
	 * @param entityInstance
	 * @param type
	 */
	public PoolSize(EntityInstance entityInstance, TypeRestriction type) {
		this(entityInstance,type,null);
	}

	public PoolSize(EntityInstance entityInstance) {
		this(entityInstance,TypeRestriction.anyType());
	}

	public EntityInstance getComponentInstance() {
		return entityInstance;
	}

	public TypeRestriction getTypeRestriction() {
		return type;
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference,
			Instance<I> actual) {

		if (this.entityInstance.equals(reference))
			return new PoolSize(
					(EntityInstance) actual,
					type,
					value);
		else
			return this;
	}

	@Override
	public void evaluateOperands() {
		if (value == null) 
			if (entityInstance instanceof EntityInstanceActual) {
				if (entityInstance.getActual() instanceof CoordinatingEntity) {
					int size = ((CoordinatingEntity) entityInstance.getActual()).getPoolSize();
					value = new NumberValue(size);
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
