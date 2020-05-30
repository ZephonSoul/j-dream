/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.expressions.EvaluationRuntimeException;
import com.dream.core.expressions.Expression;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeForAddress 
implements MapNodeInstance, Bindable<MapNodeInstance> {

	final static int BASE_CODE = 532;

	private Instance<Entity> scope;
	private Expression address;

	public MapNodeForAddress(Instance<Entity> scope, Expression address) {
		this.scope = scope;
		this.address = address;
	}

	public Expression getAddress() {
		return address;
	}

	public Instance<Entity> getScope() {
		return scope;
	}

	@Override
	public <I> MapNodeInstance bindInstance(
			Instance<I> reference,
			Instance<I> actual) {

		Expression newAddress;
		if (address.allOperandsValued())
			newAddress = address;
		else
			newAddress = address.bindInstance(reference, actual);
		Instance<Entity> newScope;
		if (scope instanceof EntityInstanceActual || scope instanceof ActualInstance<?>)
			newScope = scope;
		else
			newScope = Bindable.bindInstance(scope, reference, actual);
		
		return new MapNodeForAddress(newScope,newAddress);
	}

	@Override
	public MapNode getActual() {
		if (address.allOperandsValued()) {
			if (scope instanceof EntityInstanceActual || scope instanceof ActualInstance<?>) {
				AbstractMotif motif = (AbstractMotif) scope.getActual();
				if (motif instanceof AbstractMotif) {
					return motif.getMap().getNodeForAddress(address.eval());
				} else
					throw new IncompatibleEntityReference(scope, this.toString());
			} else
				throw new UnboundReferenceException(scope);
		} else
			throw new EvaluationRuntimeException(address);
	}

	@Override
	public String toString() {
		return String.format("%s.@(%s)", scope.toString(),address.toString());
	}

	@Override
	public int hashCode() {
		return BASE_CODE + scope.hashCode() + address.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof MapNodeForAddress) &&
				scope.equals(((MapNodeForAddress)o).getScope()) &&
				address.equals(((MapNodeForAddress)o).getAddress());
	}

	@Override
	public void evaluate() {
		address.evaluateOperands();
	}

}
