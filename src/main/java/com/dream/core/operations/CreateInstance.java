package com.dream.core.operations;

import java.lang.reflect.InvocationTargetException;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.entities.CoordinatingEntity;

/**
 * @author Alessandro Maggi
 *
 */
public class CreateInstance extends AbstractOperation {

	final static int BASE_CODE = 1000;

	private Class<? extends Entity> entityType;
	private EntityInstance parentInstance;
	private EntityInstanceRef newInstance;
	private Operation chainedOperation;

	public CreateInstance(Class<? extends Entity> entityType, 
			EntityInstance parentInstance, 
			EntityInstanceRef newInstance,
			Operation chainedOperation) {

		this.entityType = entityType;
		this.parentInstance = parentInstance;
		this.newInstance = newInstance;
		this.chainedOperation = chainedOperation;
	}

	public CreateInstance(Class<? extends Entity> entityType, 
			EntityInstance parentInstance) {
		this(entityType,parentInstance,null,Skip.getInstance());
	}
	
	public CreateInstance() {
		
	}

	/**
	 * @return the newInstance
	 */
	public EntityInstanceRef getNewInstance() {
		return newInstance;
	}

	/**
	 * @return the parentInstance
	 */
	public EntityInstance getParentInstance() {
		return parentInstance;
	}

	/**
	 * @return the entityType
	 */
	public Class<? extends Entity> getEntityType() {
		return entityType;
	}

	@Override
	public void evaluateOperands() {
		chainedOperation.evaluateOperands();
	}

	@Override
	public void execute() {
		CoordinatingEntity parentEntity = (CoordinatingEntity) parentInstance.getActual();
		if (parentEntity instanceof CoordinatingEntity)
			try {
				Entity newActualInstance = entityType.getConstructor().newInstance();
				parentEntity.addToPool(newActualInstance);
				if (newInstance instanceof EntityInstanceRef) {
					chainedOperation = chainedOperation.bindInstance(
							newInstance, new EntityInstanceActual(newActualInstance));
					chainedOperation.evaluateOperands();
				}
				chainedOperation.execute();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			throw new IllegalScopeException(parentEntity, toString());
	}

	@Override
	public int hashCode() {
		int newInstanceHashCode = 0;
		if (newInstance != null)
			newInstanceHashCode = newInstance.hashCode();			
		return BASE_CODE 
				+ entityType.hashCode() 
				+ parentInstance.hashCode()
				+ newInstanceHashCode
				+ chainedOperation.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		if (op instanceof CreateInstance && newInstance != null)
			return parentInstance.equals(((CreateInstance) op).getParentInstance())
					&& entityType.equals(((CreateInstance) op).getEntityType())
					&& newInstance.equals(((CreateInstance) op).getNewInstance());
		else
			//every instance creation is unique
			//unless it is forcefully bound to the same EntityInstanceReference
			return super.lowLevelEquals(op);
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		return new CreateInstance(
				entityType,
				parentInstance.bindInstance(reference, actual),
				newInstance,
				chainedOperation.bindInstance(reference, actual)
				);
	}

	@Override
	public void clearCache() {
		chainedOperation.clearCache();
	}

	public String toString() {
		String 	preamble = "",
				chainedOpsString = "";
		if (newInstance != null) 
			preamble = String.format("%s = ",newInstance.getName());
		if (!(chainedOperation instanceof Skip))
			chainedOpsString = String.format(";%s", chainedOperation.toString());
		return String.format("%screate(%s.%s)%s",
				preamble,
				parentInstance.toString(),
				entityType.getSimpleName(),
				chainedOpsString);

	}

}
