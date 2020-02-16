package com.ldream.ldream_core.expressions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.TypeRestriction;
import com.ldream.ldream_core.coordination.UnboundReferenceException;

public class PoolSize implements Expression {
	
	private ComponentInstance componentInstance;
	private TypeRestriction type;

	/**
	 * @param componentInstance
	 * @param type
	 */
	public PoolSize(ComponentInstance componentInstance, TypeRestriction type) {
		this.componentInstance = componentInstance;
		this.type = type;
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
					type);
		else
			return this;
	}

	@Override
	public Number eval() {
		if (componentInstance instanceof ActualComponentInstance)
			return ((ActualComponentInstance) componentInstance).getComponent().getPoolSize();
		else
			throw new UnboundReferenceException(componentInstance);
	}

	@Override
	public boolean equals(Expression ex) {
		if (ex instanceof PoolSize)
			return componentInstance.equals(((PoolSize) ex).getComponentInstance())
					&& type.equals(((PoolSize) ex).getTypeRestriction());
		else
			return false;
	}
	
	public String toString() {
		return String.format("||%s.C:%s||",
				componentInstance.getName(),
				type.toString()
				);
	}

}
