package com.dream.core.coordination.operations;

import java.lang.reflect.InvocationTargetException;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.Entity;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.CoordinatingEntity;

public class CreateInstance extends AbstractOperation implements Operation {

	final static int BASE_CODE = 1000;

	private Class<? extends Entity> entityType;
	private EntityInstance parentInstance;
	private EntityInstanceReference newInstance;
	private Operation chainedOperation;

	public CreateInstance(Class<? extends Entity> entityType, 
			EntityInstance parentInstance, 
			EntityInstanceReference newInstance,
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

	/**
	 * @return the newInstance
	 */
	public EntityInstanceReference getNewInstance() {
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
		if (parentInstance.getActualEntity() instanceof CoordinatingEntity) {
			try {
				Entity newComponent = entityType.getConstructor().newInstance();
				((CoordinatingEntity)parentInstance.getActualEntity()).addToPool(newComponent);
				if (newInstance instanceof EntityInstanceReference) {
					chainedOperation = chainedOperation.bindEntityReference(
							newInstance, new EntityInstanceActual(newComponent));
					chainedOperation.evaluateOperands();
				}
				chainedOperation.execute();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			throw new IncompatibleEntityReference(parentInstance.getActualEntity(),this.toString());
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
		return (op instanceof CreateInstance)
				&& parentInstance.equals(((CreateInstance) op).getParentInstance())
				&& entityType.equals(((CreateInstance) op).getEntityType())
				&& newInstance.equals(((CreateInstance) op).getNewInstance());
	}

	@Override
	public Operation bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		if (parentInstance.equals(entityReference)) {
			return new CreateInstance(
					entityType,
					entityActual,
					newInstance,
					chainedOperation
					);
		}
		else
			return this;
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
				parentInstance.getName(),
				entityType.getSimpleName(),
				chainedOpsString);

	}

}
