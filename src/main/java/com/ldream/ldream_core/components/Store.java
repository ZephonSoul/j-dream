package com.ldream.ldream_core.components;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Store {
	
	private Map<String,LocalVariable> localVariables;

	public Store() {
		localVariables = new HashMap<>();
	}
	
	public Store(Map<String,LocalVariable> localVariables) {
		this.localVariables = localVariables;
	}
	
	public Store(LocalVariable... localVariables) {
		this();
		for (LocalVariable lVar : localVariables)
			this.localVariables.put(lVar.getName(),lVar);
	}

	/**
	 * @return the localVariables
	 */
	public Map<String, LocalVariable> getLocalVariables() {
		return localVariables;
	}

	/**
	 * @param localVariables the localVariables to set
	 */
	public void setLocalVariables(Map<String, LocalVariable> localVariables) {
		this.localVariables = localVariables;
	}

	public void setVarValue(String varName, Number varValue) {
		if (localVariables.containsKey(varName)) {
			localVariables.get(varName).setValue(varValue);
		}
		else {
			throw new InvalidLocalVariableException(varName);
		}
	}

	public LocalVariable getLocalVariable(String varName) {
		if (localVariables.containsKey(varName)) {
			return localVariables.get(varName);
		}
		else {
			throw new InvalidLocalVariableException(varName);
		}
	}
	
	public String toString() {
		return localVariables.values().stream().map(LocalVariable::toString).collect(Collectors.joining(","));
	}
	
	public boolean equals(Store store) {
		return localVariables.equals(store.getLocalVariables());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Store)
			return equals((Store) o);
		else
			return false;
	}

}
