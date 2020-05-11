/**
 * 
 */
package com.dream.core.operations;

import java.lang.reflect.InvocationTargetException;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class CreateMotifInstance extends CreateInstance {

	private Instance<MapNode> targetMapNode;
	
	/**
	 * @param entityType
	 * @param parentInstance
	 * @param targetMapNode
	 * @param newInstance
	 * @param chainedOperation
	 */
	public CreateMotifInstance(
			Class<? extends Entity> entityType, 
			Instance<Entity> parentInstance,
			Instance<MapNode> targetMapNode,
			EntityInstanceRef newInstance, 
			Operation chainedOperation) {
		
		super(entityType, parentInstance, newInstance, chainedOperation);
		this.targetMapNode = targetMapNode;
	}

	/**
	 * @param entityType
	 * @param parentInstance
	 * @param targetMapNode
	 */
	public CreateMotifInstance(
			Class<? extends Entity> entityType, 
			Instance<Entity> parentInstance,
			Instance<MapNode> targetMapNode) {
		
		super(entityType, parentInstance);
		this.targetMapNode = targetMapNode;
	}

	/**
	 * @return the targetMapNode
	 */
	public Instance<MapNode> getTargetMapNode() {
		return targetMapNode;
	}
	
	@Override
	public void execute() {
		AbstractMotif parentEntity = (AbstractMotif) parentInstance.getActual();
		if (parentEntity instanceof AbstractMotif)
			try {
				Entity newActualInstance = entityType.getConstructor().newInstance();
				parentEntity.addToPool(newActualInstance);
				parentEntity.setEntityPosition(newActualInstance, targetMapNode.getActual());
				Operation boundOps = chainedOperation;
				if (newInstance instanceof EntityInstanceRef) {
					boundOps = chainedOperation.bindInstance(
							newInstance, new EntityInstanceActual(newActualInstance));
					boundOps.evaluateOperands();
				}
				boundOps.execute();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			throw new IllegalScopeException(parentEntity, toString());
	}
	
	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new CreateMotifInstance(
				entityType,
				bindInstance(parentInstance,reference,actual),
				bindInstance(targetMapNode,reference,actual),
				newInstance,
				chainedOperation.bindInstance(reference, actual)
				);
	}
	
	public String toString() {
		String 	preamble = "",
				chainedOpsString = "";
		if (newInstance != null) 
			preamble = String.format("%s = ",newInstance.getName());
		if (!(chainedOperation instanceof Skip))
			chainedOpsString = String.format("%s", chainedOperation.toString());
		return String.format("%screate(%s.%s@%s)[%s]",
				preamble,
				parentInstance.toString(),
				entityType.getSimpleName(),
				targetMapNode.toString(),
				chainedOpsString);

	}

}
