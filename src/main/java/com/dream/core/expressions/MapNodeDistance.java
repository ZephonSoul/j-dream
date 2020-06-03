/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Caching;
import com.dream.core.Instance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeDistance extends AbstractExpression {

	private Instance<MapNode> mapNode1;
	private Instance<MapNode> mapNode2;
	
	/**
	 * @param mapNode1
	 * @param mapNode2
	 */
	public MapNodeDistance(Instance<MapNode> mapNode1, Instance<MapNode> mapNode2) {
		this.mapNode1 = mapNode1;
		this.mapNode2 = mapNode2;
	}
	
	public Instance<MapNode> getMapNode1() {
		return mapNode1;
	}
	
	public Instance<MapNode> getMapNode2() {
		return mapNode2;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof MapNodeDistance) &&
				mapNode1.equals(((MapNodeDistance) ex).getMapNode1()) &&
				mapNode2.equals(((MapNodeDistance) ex).getMapNode2());
	}

	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		return new MapNodeDistance(
				Bindable.bindInstance(mapNode1,reference,actual),
				Bindable.bindInstance(mapNode2, reference, actual));
	}

	@Override
	public void evaluateOperands() {
		mapNode1.evaluate();
		mapNode2.evaluate();
	}

	@Override
	public void clearCache() {
		if (mapNode1 instanceof Caching)
			((Caching) mapNode1).clearCache();
		if (mapNode2 instanceof Caching)
			((Caching) mapNode2).clearCache();
	}

	@Override
	public boolean allOperandsValued() {
		return (mapNode1 instanceof MapNodeActual || mapNode1 instanceof ActualInstance<?>) 
				&& (mapNode2 instanceof MapNodeActual || mapNode2 instanceof ActualInstance<?>);
	}

	@Override
	protected Value computeResult() {
		MapNode n1 = mapNode1.getActual();
		MapNode n2 = mapNode2.getActual();
		return n1.getMap().distance(n1,n2);
	}

	@Override
	public String toString() {
		String dist = "";
		try {
			double distance = ((NumberValue) computeResult()).getRawValue().doubleValue();
			dist = String.format("[=%.2f]", distance);
		} catch(Exception ex) {}
		return String.format("%s<->%s%s", mapNode1.toString(), mapNode2.toString(), dist);
	}
	
}
