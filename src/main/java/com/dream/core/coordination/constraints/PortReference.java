package com.dream.core.coordination.constraints;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.InteractingEntity;

public class PortReference extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 25;
	
	private EntityInstanceReference entityInstance;

	private String portName;
	
	public PortReference(EntityInstanceReference entityInstance,String portName) {
		this.entityInstance = entityInstance;
		this.portName = portName;
	}
	/**
	 * @return the componentInstance
	 */
	public EntityInstanceReference getEntityInstance() {
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
	public Formula bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {
		
		if (entityReference.equals(this.entityInstance)) {
			if (entityActual.getActualEntity() instanceof InteractingEntity)
				return new PortAtom(
						((InteractingEntity)entityActual.getActualEntity())
						.getPortByName(portName));
			else
				throw new IncompatibleEntityReference(entityActual.getActualEntity(),this.toString());
		} else
			return this;
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

}
