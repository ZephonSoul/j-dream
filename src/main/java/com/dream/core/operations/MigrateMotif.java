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
public class MigrateMotif extends Migrate {

	protected Instance<MapNode> targetNode;
	
	/**
	 * @param entity
	 * @param targetMotif
	 * @param targetNode
	 */
	public MigrateMotif(
			Instance<Entity> entity, 
			Instance<Entity> targetMotif,
			Instance<MapNode> targetNode) {
		
		super(entity, targetMotif);
		this.targetNode = targetNode;
	}
	
	public Instance<MapNode> getTargetNode() {
		return targetNode;
	}
	
	@Override
	public void execute() {
		AbstractMotif targetMotif = (AbstractMotif)targetParent.getActual();
		if (targetMotif instanceof AbstractMotif) {
			super.execute();
			targetMotif.setEntityPosition(
					entity.getActual(), targetNode.getActual());
		} else
			throw new IncompatibleEntityReference(targetParent, this.toString());
	}

	@Override
	public int hashCode() {
		return super.hashCode()
				+ targetNode.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return super.equals(op)
				&& (op instanceof MigrateMotif)
				&& targetNode.equals(((MigrateMotif) op).getTargetNode());
	}
	

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		return new MigrateMotif(
				bindInstance(entity, reference, actual),
				bindInstance(targetParent, reference, actual),
				bindInstance(targetNode, reference, actual));
	}
	
	public String toString() {
		return String.format("migrate(%s,%s,%s)",
				entity.toString(),
				targetParent.toString(),
				targetNode.toString());
	}
	
}
