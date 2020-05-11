/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.NodeNotFoundException;
import com.dream.core.expressions.Expression;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeVarEquals implements MapNodeInstance, Bindable<MapNodeInstance> {

	private Instance<Entity> scope;
	private String varName;
	private Expression testVarExpression;
	
	public MapNodeVarEquals(
			Instance<Entity> scope,
			String varName,
			Expression testVarExpression) {
		
		this.scope = scope;
		this.varName = varName;
		this.testVarExpression = testVarExpression;
	}

	@Override
	public MapNode getActual() {
		MapNode node = null;
		try {
			node = ((AbstractMotif) scope.getActual()).getMap().getNodeVarEquals(varName,testVarExpression.eval());
		} catch (NodeNotFoundException ex) {
			//ex.printStackTrace();
		}
		return node;
	}

	@Override
	public <I> MapNodeInstance bindInstance(Instance<I> reference, Instance<I> actual) {
		Instance<Entity> newScope = Bindable.bindInstance(scope, reference, actual);
		Expression newExpression = testVarExpression.bindInstance(reference, actual);
		MapNode node = null;
		try {
			//TODO: insert check for AbstractMotif instance
			node = ((AbstractMotif)newScope.getActual()).getMap().getNodeVarEquals(varName,newExpression.eval());
		} catch (NodeNotFoundException | UnboundReferenceException ex) {
//			ex.printStackTrace();
//		} catch (UnboundReferenceException ex) {
			return new MapNodeVarEquals(newScope,varName,newExpression);
		}
		return new MapNodeActual(node);
	}
	
	public String toString() {
		return String.format("%s.MapNode(%s=%s)",
				scope.toString(),
				varName,
				testVarExpression.toString());
	}

}
