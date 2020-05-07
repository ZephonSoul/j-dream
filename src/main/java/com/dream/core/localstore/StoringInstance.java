/**
 * 
 */
package com.dream.core.localstore;

/**
 * @author Alessandro Maggi
 *
 */
public interface StoringInstance {

	public VarStore getStore();
	
	public LocalVariable getVariable(String variableName);
	
}
