/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public class CreateMapNode extends AbstractOperation {

	protected EntityInstance entity;
	protected MapNodeRef newMapNode;
	protected Operation chainedOperation;
	
	public CreateMapNode(
			EntityInstance entity,
			MapNodeRef newMapNode,
			Operation chainedOperation) {
		
		this.entity = entity;
		this.newMapNode = newMapNode;
		this.chainedOperation = chainedOperation;
	}
	
	public CreateMapNode(EntityInstance entity) {
		this(entity,null,Skip.getInstance());
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		return new CreateMapNode(
				entity.bindInstance(reference, actual),
				newMapNode,
				chainedOperation.bindInstance(reference, actual));
	}

	@Override
	public void clearCache() {
		chainedOperation.clearCache();
	}

	@Override
	public void evaluateOperands() {
		chainedOperation.evaluateOperands();
	}

	@Override
	public void execute() {
		AbstractMotif motif = (AbstractMotif) entity.getActual();
		if (motif instanceof AbstractMotif) {
			chainedOperation.bindInstance(newMapNode,new MapNodeActual(motif.getMap().createNode()));
			chainedOperation.evaluateOperands();
			chainedOperation.execute();
		} else
			throw new IllegalScopeException(entity.getActual(),this.toString());
	}

	@Override
	public boolean equals(Operation op) {
		return super.lowLevelEquals(op);
	}

}
