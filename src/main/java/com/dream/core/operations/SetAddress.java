/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public class SetAddress extends AbstractOperation {

	private EntityInstance entity;
	private MapNodeInstance mapNode;

	public SetAddress(
			EntityInstance entity,
			MapNodeInstance mapNode) {

		this.entity = entity;
		this.mapNode = mapNode;
	}
	
	public EntityInstance getEntity() {
		return entity;
	}
	
	public MapNodeInstance getMapNode() {
		return mapNode;
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference,
			Instance<I> actual) {
		
		return new SetAddress(
				entity.bindInstance(reference, actual),
				mapNode.bindInstance(reference, actual));
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
