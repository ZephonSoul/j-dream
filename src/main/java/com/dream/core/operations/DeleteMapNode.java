/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class DeleteMapNode extends AbstractOperation {

	protected MapNodeInstance mapNode;
	
	public DeleteMapNode(MapNodeInstance mapNode) {
		this.mapNode = mapNode;
	}
	
	public MapNodeInstance getMapNode() {
		return mapNode;
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		if (mapNode instanceof MapNodeActual)
			return this;
		else
			return new DeleteMapNode(
					mapNode.bindInstance(reference, actual));
	}

	@Override
	public void clearCache() {}

	@Override
	public void evaluateOperands() {}

	@Override
	public void execute() {
		MapNode actual = mapNode.getActual();
		actual.getMap().deleteNode(actual);
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof DeleteMapNode) &&
				((DeleteMapNode)op).getMapNode().equals(mapNode);
	}

}
