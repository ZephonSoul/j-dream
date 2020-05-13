/**
 * 
 */
package com.dream.core.localstore;

import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public interface StoringInstance {

	public VarStore getStore();
	
	public LocalVariable getVariable(String variableName);
	
	public default void setVariable(String variableName, Value variableValue) {
		getStore().setVarValue(variableName, variableValue);
	}
	
}
