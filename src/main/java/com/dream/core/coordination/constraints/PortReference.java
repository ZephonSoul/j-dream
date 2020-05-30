package com.dream.core.coordination.constraints;

import com.dream.core.ActualInstance;
import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.entities.Port;

/**
 * @author Alessandro Maggi
 *
 */
public class PortReference extends AbstractFormula implements Instance<Port> {
	
	private static final int BASE_CODE = 25;
	
	private Instance<Entity> entityInstance;

	private String portName;
	
	public PortReference(Instance<Entity> entityInstance,String portName) {
		this.entityInstance = entityInstance;
		this.portName = portName;
	}
	/**
	 * @return the componentInstance
	 */
	public Instance<Entity> getEntityInstance() {
		return entityInstance;
	}

	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

	@Override
	public boolean sat(Interaction i) {
		throw new UnboundReferenceException(entityInstance);
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s", 
				entityInstance.toString(),
				portName);
	}
	
	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof PortReference)
			&& entityInstance.equals(((PortReference) formula).getEntityInstance())
			&& portName.equals(((PortReference) formula).getPortName());
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + entityInstance.hashCode() + portName.hashCode();
	}
	
	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		Instance<Entity> newInstance = Bindable.bindInstance(entityInstance,reference,actual);
		//TODO: streamline actual instances
		if ((newInstance instanceof EntityInstanceActual) || (newInstance instanceof ActualInstance<?>)) {
			if (newInstance.getActual() instanceof InteractingEntity)
				return new PortAtom(
						((InteractingEntity)newInstance.getActual())
						.getPortByName(portName));
			else
				throw new IncompatibleEntityReference(actual,this.toString());
		} else
			return new PortReference(newInstance,portName);
	}
	
	@Override
	public Port getActual() {
		throw new UnboundReferenceException(entityInstance);
	}
	
	@Override
	public void evaluate() {
		entityInstance.evaluate();
	}

}
