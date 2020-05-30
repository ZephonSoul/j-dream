/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.ActualInstance;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class Move extends MigrateMotif {

	/**
	 * @param entity
	 * @param targetNode
	 */
	public Move(Instance<Entity> entity, Instance<MapNode> targetNode) {
		super(entity, null, targetNode);
	}

	@Override
	public void execute() {
		if (entity instanceof ActualInstance<?> || entity instanceof EntityInstanceActual) {
			targetParent = new ActualInstance<Entity>(entity.getActual().getParent());
			super.execute();
		} else
			throw new UnboundReferenceException(entity);
	}

	@Override
	public int hashCode() {
		return entity.hashCode() + targetNode.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof Move)
				&& entity.equals(((Move) op).getEntity())
				&& targetNode.equals(((Move) op).getTargetNode());
	}
	
	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		return new Move(
				bindInstance(entity, reference, actual),
				bindInstance(targetNode, reference, actual));
	}

	
	public String toString() {
		return String.format("move(%s,%s)",
				entity.toString(),
				targetNode.toString());
	}
	
}
