package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public class MigrateMotif extends Migrate {

	protected MapNodeInstance targetNode;
	
	/**
	 * @param entity
	 * @param targetMotif
	 * @param targetNode
	 */
	public MigrateMotif(
			EntityInstance entity, 
			EntityInstance targetMotif,
			MapNodeInstance targetNode) {
		
		super(entity, targetMotif);
		this.targetNode = targetNode;
	}
	
	public MapNodeInstance getTargetNode() {
		return targetNode;
	}
	
	@Override
	public void execute() {
		AbstractMotif motif = (AbstractMotif)targetParent.getActual();
		if (motif instanceof AbstractMotif) {
			motif.setEntityPosition(
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
				entity.bindInstance(reference, actual),
				targetParent.bindInstance(reference, actual),
				targetNode.bindInstance(reference, actual));
	}
	
	public String toString() {
		return String.format("migrate(%s,%s,%s)",
				entity.toString(),
				targetParent.toString(),
				targetNode.toString());
	}
	
}
