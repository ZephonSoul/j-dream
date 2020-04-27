package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.entities.LocalVariable;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.VariableRef;

public class Assign extends AbstractOperation {

	final static int BASE_CODE = 100;

	private Instance<LocalVariable> localVariable;
	private Expression valueExpression;

	public Assign(Instance<LocalVariable> localVariable, Expression valueExpression) {
		this.localVariable = localVariable;
		this.valueExpression = valueExpression;
	}

	/**
	 * @return the valueExpression
	 */
	public Expression getValueExpression() {
		return valueExpression;
	}

	/**
	 * @return the localVariable
	 */
	public Instance<LocalVariable> getLocalVariable() {
		return localVariable;
	}

	@Override
	public void evaluateOperands() {
		valueExpression.evaluateOperands();
	}

	@Override
	public void execute() {
		if (localVariable instanceof VariableActual)
			((VariableActual)localVariable).getLocalVariable().setValue(valueExpression.eval());
		//TODO: raise exception otherwise
	}

	@Override
	public int hashCode() {
		return BASE_CODE + localVariable.hashCode() + valueExpression.hashCode();
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (localVariable instanceof VariableActual)
			return new Assign(
					localVariable,
					valueExpression.bindInstance(
							reference, actual));
		else {
			Instance<LocalVariable> boundLocalVariable;
			Expression lVarBound = ((VariableRef) localVariable).bindInstance(
					reference, actual);
			if (lVarBound instanceof VariableActual)
				boundLocalVariable = (VariableActual) lVarBound;
			else
				boundLocalVariable = localVariable;

			return new Assign(
					boundLocalVariable,
					valueExpression.bindInstance(
							reference, actual));
		}
	}

	@Override
	public void clearCache() {
		valueExpression.clearCache();
	}

	public boolean equals(Operation op) {
		return (op instanceof Assign) 
				&& (valueExpression.equals(((Assign)op).getValueExpression())) 
				&& (localVariable.equals(((Assign)op).getLocalVariable()));
	}

	public String toString() {
		return String.format("assign(%s,%s)",
				localVariable.toString(),
				valueExpression.toString());
	}

}
