/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Instance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeAddress extends AbstractExpression {

	private Instance<MapNode> mapNode;

	public MapNodeAddress(Instance<MapNode> mapNode) {
		this.mapNode = mapNode;
	}

	public Instance<MapNode> getMapNode() {
		return mapNode;
	}

	@Override
	public void evaluateOperands() {
		// TODO Auto-generated method stub
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof MapNodeAddress) &&
				mapNode.equals(((MapNodeAddress)ex).getMapNode());
	}

	@Override
	public boolean allOperandsValued() {
		return (mapNode instanceof ActualInstance<?> || mapNode instanceof MapNodeActual);
	}

	@Override
	protected Value computeResult() {
		MapNode node = mapNode.getActual();
		return node.getMap().getAddressForNode(node);
	}
	
	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		if (mapNode instanceof ActualInstance<?>)
			return this;
		else
			return new MapNodeAddress(Bindable.bindInstance(mapNode,reference,actual));
	}
	
	@Override
	public String toString() {
		return String.format("addr(%s)", mapNode.toString());
	}

}
