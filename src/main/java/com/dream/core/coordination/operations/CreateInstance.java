package com.dream.core.coordination.operations;

import java.lang.reflect.InvocationTargetException;

import com.dream.core.components.Component;
import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.ReferencedComponentInstance;

public class CreateInstance extends AbstractOperation implements Operation {

	final static int BASE_CODE = 1000;

	private Class<? extends Component> componentType;
	private ComponentInstance parentInstance;
	private ReferencedComponentInstance newInstance;
	private Operation chainedOperation;

	public CreateInstance(Class<? extends Component> componentType, 
			ComponentInstance parentInstance, 
			ReferencedComponentInstance newInstance,
			Operation chainedOperation) {

		this.componentType = componentType;
		this.parentInstance = parentInstance;
		this.newInstance = newInstance;
		this.chainedOperation = chainedOperation;
	}

	public CreateInstance(Class<? extends Component> componentType, 
			ComponentInstance parentInstance) {
		this(componentType,parentInstance,null,Skip.getInstance());
	}

	/**
	 * @return the newInstance
	 */
	public ReferencedComponentInstance getNewInstance() {
		return newInstance;
	}

	/**
	 * @return the parentInstance
	 */
	public ComponentInstance getParentInstance() {
		return parentInstance;
	}

	/**
	 * @return the componentType
	 */
	public Class<? extends Component> getComponentType() {
		return componentType;
	}

	@Override
	public void evaluateOperands() {
		chainedOperation.evaluateOperands();
	}

	@Override
	public void execute() {
		try {
			Component newComponent = componentType.getConstructor().newInstance();
			parentInstance.getComponent().addToPool(newComponent);
			if (newInstance instanceof ReferencedComponentInstance) {
				chainedOperation = chainedOperation.bindActualComponent(
						newInstance, new ActualComponentInstance(newComponent));
				chainedOperation.evaluateOperands();
			}
			chainedOperation.execute();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int hashCode() {
		int newInstanceHashCode = 0;
		if (newInstance != null)
			newInstanceHashCode = newInstance.hashCode();			
		return BASE_CODE 
				+ componentType.hashCode() 
				+ parentInstance.hashCode()
				+ newInstanceHashCode
				+ chainedOperation.hashCode();
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof CreateInstance)
				&& parentInstance.equals(((CreateInstance) op).getParentInstance())
				&& componentType.equals(((CreateInstance) op).getComponentType())
				&& newInstance.equals(((CreateInstance) op).getNewInstance());
	}

	@Override
	public Operation bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (parentInstance.equals(componentReference))
			return new CreateInstance(
					componentType,
					actualComponent,
					newInstance,
					chainedOperation
					);
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
				componentType.getSimpleName(),
				chainedOpsString);

	}

}
