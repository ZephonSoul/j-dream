/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public class CreateMapNode extends AbstractOperation {

	protected Instance<Entity> mapScope;
	protected MapNodeRef newMapNode;
	protected Operation chainedOperation;
	
	public CreateMapNode(
			Instance<Entity> mapScope,
			MapNodeRef newMapNode,
			Operation chainedOperation) {
		
		this.mapScope = mapScope;
		this.newMapNode = newMapNode;
		this.chainedOperation = chainedOperation;
	}
	
	public CreateMapNode(Instance<Entity> entity) {
		this(entity,null,Skip.getInstance());
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		return new CreateMapNode(
				bindInstance(mapScope,reference,actual),
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
		AbstractMotif motif = (AbstractMotif) mapScope.getActual();
		if (motif instanceof AbstractMotif) {
			Operation boundOps = chainedOperation.bindInstance(newMapNode,new MapNodeActual(motif.createMapNode()));
			boundOps.evaluateOperands();
			boundOps.execute();
		} else
			throw new IllegalScopeException(mapScope.getActual(),this.toString());
	}

	@Override
	public boolean equals(Operation op) {
		return super.lowLevelEquals(op);
	}
	
	@Override
	public String toString() {
		String output = "";
		if (newMapNode == null)
			output = String.format("addNode(%s)", mapScope.toString());
		else
			output = String.format("addNode(%s,%s)", 
					mapScope.toString(),
					newMapNode.toString());
		if (!chainedOperation.equals(Skip.getInstance()))
			output += String.format("[%s]", chainedOperation.toString());
		return output;
	}

}
