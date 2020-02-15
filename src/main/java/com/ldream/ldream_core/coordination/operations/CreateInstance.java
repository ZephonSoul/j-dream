package com.ldream.ldream_core.coordination.operations;

import java.lang.reflect.InvocationTargetException;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class CreateInstance extends AbstractOperation implements Operation {

	public static int BASE_CODE = 1000;

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
		this(componentType,parentInstance,null,new Skip());
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
	public void evaluateParams() {
		chainedOperation.evaluateParams();
	}

	@Override
	public void execute() {
		try {
			Component newComponent = componentType.getConstructor().newInstance();
			parentInstance.getComponent().addToPool(newComponent);
			if (newInstance instanceof ReferencedComponentInstance)
				chainedOperation.bindActualComponent(newInstance, new ActualComponentInstance(newComponent));
			chainedOperation.execute();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
