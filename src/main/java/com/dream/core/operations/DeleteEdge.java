package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class DeleteEdge extends AbstractOperation {

	private Instance<MapNode> node1,node2;
	
	public DeleteEdge(Instance<MapNode> node1, Instance<MapNode> node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Instance<MapNode> getNode1() {
		return node1;
	}
	
	public Instance<MapNode> getNode2() {
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
					bindInstance(node1,reference, instance),
					bindInstance(node2,reference, instance));
	}

	@Override
	public void clearCache() {}

	@Override
	public void evaluate() {
		node1.evaluate();
		node2.evaluate();
	}

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
