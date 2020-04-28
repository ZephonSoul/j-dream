package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.entities.LocalVariable;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class VariableRef 
extends AbstractExpression implements Instance<LocalVariable> {

	private Instance<?> scope;
	private String localVariableName;

	public VariableRef(
			Instance<?> scope, 
			String localVariableName) {

		this.scope = scope;
		this.localVariableName = localVariableName;
	}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof VariableRef) 
				&& (scope.equals(((VariableRef) ex).getScope()))
				&& (localVariableName.equals(((VariableRef) ex).getVariableName()));
	}

	@Override
	public <I> Expression bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (this.scope.equals(reference)) {
			LocalVariable lvar;
			if (actual.getActual() instanceof InteractingEntity)
				lvar = ((InteractingEntity)actual.getActual()).getStore()
				.getLocalVariable(localVariableName);
			else if (actual.getActual() instanceof MapNode)
				lvar = ((MapNode)actual.getActual()).getStore()
				.getLocalVariable(localVariableName);
			else
				throw new IncompatibleEntityReference(actual, this.toString());
			return new VariableActual(lvar);
		} else
			return this;
	}

	/**
	 * @return the componentVariable
	 */
	public Instance<?> getScope() {
		return scope;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return localVariableName;
	}

	@Override
	public String toString() {
		return String.format("%s.%s",scope.getName(),localVariableName);
	}

	@Override
	public int hashCode() {
		return scope.hashCode() + localVariableName.hashCode();
	}

	@Override
	public void evaluateOperands() {	}

	@Override
	public Value computeResult() {
		throw new UnboundReferenceException(scope);
	}

	@Override
	public boolean allOperandsValued() {
		return false;
	}

	@Override
	public void clearCache() {}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public LocalVariable getActual() {
		throw new UnboundReferenceException(scope);
	}


}
