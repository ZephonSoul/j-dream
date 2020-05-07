/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.Instance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.expressions.values.Value;
import com.dream.core.localstore.LocalVariable;

/**
 * @author Alessandro Maggi
 *
 */
public class VariableMapNodeRef 
extends AbstractExpression implements Instance<LocalVariable> {

	private MapNodeInstance node;
	private String variableName;
	private Value variableValue;
	
	public VariableMapNodeRef(MapNodeInstance node, String variableName) {
		this.node = node;
		this.variableName = variableName;
	}

	/**
	 * @return the node
	 */
	public MapNodeInstance getMapNode() {
		return node;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}

	@Override
	public void evaluateOperands() {
		MapNodeActual nodeActual = (MapNodeActual) node;
		if (nodeActual instanceof MapNodeActual) {
			variableValue = nodeActual.getActual().getStore().getLocalVariable(variableName).getValue();
		} else
			throw new UnboundReferenceException(node);
	}

	@Override
	public void clearCache() {}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof VariableMapNodeRef) 
				&& ((VariableMapNodeRef)ex).getMapNode().equals(node)
				&& ((VariableMapNodeRef)ex).getVariableName().equals(variableName);
	}

	@Override
	protected Value computeResult() {
		if (variableValue == null)
			evaluateOperands();
		return variableValue;
	}

	@Override
	protected boolean allOperandsValued() {
		return false;
	}

	@Override
	public LocalVariable getActual() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <I> Expression bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (allOperandsValued())
			return this;
		else
			return new VariableMapNodeRef(node.bindInstance(reference, actual), variableName);
	}

}
