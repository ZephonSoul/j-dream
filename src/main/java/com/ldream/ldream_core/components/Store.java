package com.ldream.ldream_core.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Store {

	private Map<String,LocalVariable> localVariables;
	private Component owner;

	public Store() {
		this.localVariables = new HashMap<>();
	}

	public Store(Component owner, Map<String,LocalVariable> localVariables) {
		this.owner = owner;
		this.localVariables = localVariables;
		bindOwner();
	}

	public Store(Component owner) {
		this(owner, new HashMap<>());
	}

	public Store(Component owner, LocalVariable... localVariables) {
		this(owner,
				Arrays.stream(localVariables)
				.collect(Collectors.toMap(LocalVariable::getName, Function.identity())));
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

	/**
	 * @return the owner
	 */
	public Component getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Component owner) {
		this.owner = owner;
		bindOwner();
	}

	private void bindOwner() {
		localVariables.values().stream().forEach(lv -> lv.setOwner(owner));
	}

	public String toString() {
		return localVariables.values().stream().map(LocalVariable::toString).collect(Collectors.joining(","));
	}

	public boolean equals(Store store) {
		return localVariables.equals(store.getLocalVariables());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Store) && equals((Store) o);
	}

}
