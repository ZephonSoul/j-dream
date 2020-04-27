package com.dream.core.coordination.constraints;

import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.entities.Port;

public class PortReference extends AbstractFormula implements Instance<Port> {
	
	private static final int BASE_CODE = 25;
	
	private EntityInstanceRef entityInstance;

	private String portName;
	
	public PortReference(EntityInstanceRef entityInstance,String portName) {
		this.entityInstance = entityInstance;
		this.portName = portName;
	}
	/**
	 * @return the componentInstance
	 */
	public EntityInstanceRef getEntityInstance() {
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

//	@Override
//	public Formula bindEntityReference(
//			EntityInstanceRef entityReference, 
//			EntityInstanceActual entityActual) {
//		
//		if (entityReference.equals(this.entityInstance)) {
//			if (entityActual.getActualEntity() instanceof InteractingEntity)
//				return new PortAtom(
//						((InteractingEntity)entityActual.getActualEntity())
//						.getPortByName(portName));
//			else
//				throw new IncompatibleEntityReference(entityActual.getActualEntity(),this.toString());
//		} else
//			return this;
//	}
	
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
		if (reference.equals(this.entityInstance)) {
			if (actual.getActual() instanceof InteractingEntity)
				return new PortAtom(
						((InteractingEntity)actual.getActual())
						.getPortByName(portName));
			else
				throw new IncompatibleEntityReference(actual,this.toString());
		} else
			return this;
	}
	@Override
	public String getName() {
		return toString();
	}
	@Override
	public Port getActual() {
		throw new UnboundReferenceException(entityInstance);
	}

}
