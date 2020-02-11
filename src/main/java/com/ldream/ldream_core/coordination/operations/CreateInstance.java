package com.ldream.ldream_core.coordination.operations;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;

public class CreateInstance implements Operation {

	public static int BASE_CODE = 1000;

	private ComponentInstance newInstance;
	private ComponentInstance parentInstance;
	private Class<? extends Component> componentType;
	
	public CreateInstance(Class<? extends Component> componentType, 
			ComponentInstance parentInstance, 
			ComponentInstance newInstance) {
		this.componentType = componentType;
		this.parentInstance = parentInstance;
		this.newInstance = newInstance;
	}
	
	public CreateInstance(Class<? extends Component> componentType, 
			ComponentInstance parentInstance) {
		this(componentType,parentInstance,null);
	}
	
	@Override
	public void evaluateParams() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equals(Operation op) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Operation bindActualComponent(ComponentInstance componentVariable, Component actualComponent) {
		// TODO Auto-generated method stub
		return null;
	}

}
