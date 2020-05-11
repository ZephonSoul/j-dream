/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
@Deprecated
public class VariableMapProperty extends AbstractExpression {

	private EntityInstance scope;
	private String propertyName;
	private String variableName;
	private Value variableValue;

	public VariableMapProperty(
			EntityInstance scope, 
			String propertyName,
			String variableName) {

		this.scope = scope;
		this.propertyName = propertyName;
		this.variableName = variableName;
	}

	public EntityInstance getScope() {
		return scope;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getVariableName() {
		return variableName;
	}

	@Override
	public void evaluateOperands() {
		AbstractMotif motif = (AbstractMotif)scope.getActual();
		if (motif instanceof AbstractMotif) {
			InteractingEntity entity = (InteractingEntity) motif.getMapProperty(propertyName).get();
			if (entity instanceof InteractingEntity)
				variableValue = entity.getStore().getLocalVariable(variableName).getValue();
			else
				throw new IncompatibleEntityReference(
						new EntityInstanceActual(entity), this.toString());
		} else
			throw new IllegalScopeException(motif, this.toString());
	}

	@Override
	public void clearCache() {
		variableValue = null;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof VariableMapProperty)
				&& ((VariableMapProperty)ex).getScope().equals(scope)
				&& ((VariableMapProperty)ex).getPropertyName().equals(propertyName)
				&& ((VariableMapProperty)ex).getVariableName().equals(variableName);
	}

	@Override
	protected Value computeResult() {
		if (variableValue == null)
			evaluateOperands();
		return variableValue;
	}

	@Override
	public boolean allOperandsValued() {
		return (variableValue != null);
	}

	@Override
	public String toString() {
		if (allOperandsValued())
			return variableValue.toString();
		else
			return String.format("%s(%s).%s", 
					propertyName, 
					scope.toString(), 
					variableName);
	}

	@Override
	public <I> Expression bindInstance(Instance<I> reference, Instance<I> actual) {
		if (scope.equals(reference))
			return new VariableMapProperty(
					(EntityInstance) actual, 
					propertyName, 
					variableName);
		else
			return this;
	}

}
