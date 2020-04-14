package com.dream.core.expressions;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.expressions.values.Value;

public class ReferencedVariable extends AbstractExpression implements VariableExpression {

	private EntityInstance entityInstance;
	private String localVariableName;

	public ReferencedVariable(
			EntityInstance entityInstance, 
			String localVariableName) {

		this.entityInstance = entityInstance;
		this.localVariableName = localVariableName;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof ReferencedVariable) 
				&& (entityInstance.equals(((ReferencedVariable) ex).getEntityInstance()))
				&& (localVariableName.equals(((ReferencedVariable) ex).getVariableName()));
	}

	@Override
	public Expression bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual) {

		if (this.entityInstance.equals(entityReference)) {
			if (entityActual.getActualEntity() instanceof InteractingEntity)
				return new ActualVariable(
						((InteractingEntity)entityActual.getActualEntity()).getStore()
						.getLocalVariable(localVariableName));
			else
				throw new IncompatibleEntityReference(entityActual.getActualEntity(), this.toString());
		} else
			return this;
	}

	/**
	 * @return the componentVariable
	 */
	public EntityInstance getEntityInstance() {
		return entityInstance;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return localVariableName;
	}

	@Override
	public String toString() {
		return String.format("%s.%s",entityInstance.getName(),localVariableName);
	}

	@Override
	public int hashCode() {
		return entityInstance.hashCode() + localVariableName.hashCode();
	}

	@Override
	public void evaluateOperands() {	}

	@Override
	public Value computeResult() {
		throw new UnboundReferenceException(entityInstance);
	}

	@Override
	public boolean allOperandsValued() {
		return false;
	}

	@Override
	public void clearCache() {}

}
