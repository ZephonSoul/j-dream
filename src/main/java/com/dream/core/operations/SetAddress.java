/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class SetAddress extends AbstractOperation {

	private Instance<Entity> entity;
	private Instance<MapNode> mapNode;

	public SetAddress(
			Instance<Entity> entity,
			Instance<MapNode> mapNode) {

		this.entity = entity;
		this.mapNode = mapNode;
	}
	
	public Instance<Entity> getEntity() {
		return entity;
	}
	
	public Instance<MapNode> getMapNode() {
		return mapNode;
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference,
			Instance<I> actual) {
		
		return new SetAddress(
				bindInstance(entity,reference, actual),
				bindInstance(mapNode,reference, actual));
	}

	@Override
	public void clearCache() {}

	@Override
	public void evaluateOperands() {}

	@Override
	public void execute() {
		// conditions: entity hosted in the map's motif
		Entity actual = entity.getActual();
		if (actual.getParent() instanceof AbstractMotif) {
			AbstractMotif motif = (AbstractMotif)actual.getParent();
			if (motif.hosts(actual))
				motif.setEntityPosition(actual, mapNode.getActual());
		} else
			throw new IncompatibleEntityReference(entity, this.toString());
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof SetAddress)
				&& ((SetAddress)op).getEntity().equals(entity)
				&& ((SetAddress)op).getMapNode().equals(mapNode);
	}

}
