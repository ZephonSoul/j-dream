package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.TypeRestriction;
import com.ldream.ldream_core.coordination.UnboundReferenceException;
import com.ldream.ldream_core.expressions.values.NumberValue;
import com.ldream.ldream_core.expressions.values.Value;

public class PoolSize extends AbstractExpression implements Expression {
	
	public static final int BASE_CODE = 31;

	private ComponentInstance componentInstance;
	private TypeRestriction type;
	private Value value;

	/**
	 * @param componentInstance
	 * @param type
	 * @param value
	 */
	public PoolSize(
			ComponentInstance componentInstance, 
			TypeRestriction type, 
			Value value) {
		this.componentInstance = componentInstance;
		this.type = type;
		this.value = value;
	}

	/**
	 * @param componentInstance
	 * @param type
	 */
	public PoolSize(ComponentInstance componentInstance, TypeRestriction type) {
		this(componentInstance,type,null);
	}

	public PoolSize(ComponentInstance componentInstance) {
		this(componentInstance,TypeRestriction.anyType());
	}

	public ComponentInstance getComponentInstance() {
		return componentInstance;
	}

	public TypeRestriction getTypeRestriction() {
		return type;
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentReference,
			ActualComponentInstance actualComponent) {

		if (this.componentInstance.equals(componentReference))
			return new PoolSize(
					actualComponent,
					type,
					value);
		else
			return this;
	}

	@Override
	public void evaluateOperands() {
		if (value == null)
			if (componentInstance instanceof ActualComponentInstance) {
				int size = ((ActualComponentInstance) componentInstance).getComponent().getPoolSize();
				value = new NumberValue(size);
				}
			else
				throw new UnboundReferenceException(componentInstance);
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
				&& componentInstance.equals(((PoolSize) ex).getComponentInstance())
				&& type.equals(((PoolSize) ex).getTypeRestriction());
	}

	public String toString() {
		return String.format("size(%s.C:%s)",
				componentInstance.getName(),
				type.toString()
				);
	}

	@Override
	public void clearCache() {
		value = null;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + componentInstance.hashCode() + type.hashCode();
	}

}
