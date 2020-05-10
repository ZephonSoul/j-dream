package com.dream.core.expressions;

import com.dream.core.Bindable;
import com.dream.core.Instance;
import com.dream.core.coordination.IllegalScopeException;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.expressions.values.Value;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.StoringInstance;

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
	
	public VariableRef(String localVariableName) {
		this(null, localVariableName);
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

		Instance<?> newScope = Bindable.bindInstance(scope,reference,actual);
		
		if (newScope instanceof Bindable<?>)
			return new VariableRef(newScope,localVariableName);
		else {
			StoringInstance actualInstance = (StoringInstance) newScope.getActual();
			if (actualInstance instanceof StoringInstance)
				return new VariableActual(actualInstance.getVariable(localVariableName));
			else
				throw new IllegalScopeException(actual,this.toString());
		}
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
		String scopeString = "";
		if (scope != null)
			scopeString = scope.toString() + ".";
		return String.format("%s%s",scopeString,localVariableName);
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
	public LocalVariable getActual() {
		throw new UnboundReferenceException(scope);
	}


}
