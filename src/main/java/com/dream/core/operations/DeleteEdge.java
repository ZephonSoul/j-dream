package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeInstance;

/**
 * @author Alessandro Maggi
 *
 */
public class DeleteEdge extends AbstractOperation {

	private MapNodeInstance node1,node2;
	
	public DeleteEdge(MapNodeInstance node1, MapNodeInstance node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public MapNodeInstance getNode1() {
		return node1;
	}
	
	public MapNodeInstance getNode2() {
		return node2;
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> instance) {
		
		if (node1 instanceof MapNodeActual && node2 instanceof MapNodeActual)
			return this;
		else
			return new DeleteEdge(
					node1.bindInstance(reference, instance),
					node2.bindInstance(reference, instance));
	}

	@Override
	public void clearCache() {}

	@Override
	public void evaluateOperands() {	}

	@Override
	public void execute() {
		node1.getActual().getMap().createEdge(
				node1.getActual(), node2.getActual());
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof DeleteEdge)
				&& ((DeleteEdge)op).getNode1().equals(node1)
				&& ((DeleteEdge)op).getNode2().equals(node2);
	}

}
