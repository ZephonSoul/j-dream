package com.dream.core.localstore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class VarStore {

	private Map<String,LocalVariable> localVariables;
	private StoringInstance owner;

	public VarStore() {
		this.localVariables = new HashMap<>();
	}

	public VarStore(StoringInstance owner, Map<String,LocalVariable> localVariables) {
		this.owner = owner;
		this.localVariables = localVariables;
		bindOwner();
	}

	public VarStore(StoringInstance owner) {
		this(owner, new HashMap<>());
	}

	public VarStore(StoringInstance owner, LocalVariable... localVariables) {
		this(owner,
				Arrays.stream(localVariables)
				.collect(Collectors.toMap(LocalVariable::getName, Function.identity())));
	}

	public VarStore(LocalVariable... localVariables) {
		this.localVariables = Arrays.stream(localVariables)
				.collect(Collectors.toMap(LocalVariable::getName, Function.identity()));
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

	public void setVarValue(String varName, Value varValue) {
		if (localVariables.containsKey(varName))
			localVariables.get(varName).setValue(varValue);
		else 
			// create new variable if not present
			localVariables.put(varName,new LocalVariable(varName,varValue,owner));
	}
	
	public boolean hasLocalVariable(String varName) {
		return localVariables.containsKey(varName);
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
	public StoringInstance getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(StoringInstance owner) {
		this.owner = owner;
		bindOwner();
	}

	private void bindOwner() {
		localVariables.values().stream().forEach(lv -> lv.setOwner(owner));
	}

	public String toString() {
		return localVariables.values().stream().map(LocalVariable::toString).collect(Collectors.joining(","));
	}

	public boolean equals(VarStore store) {
		return localVariables.equals(store.getLocalVariables());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof VarStore) && equals((VarStore) o);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {		
		//JSONArray descriptor = new JSONArray();
		JSONObject descriptor = new JSONObject();
		
		localVariables.values().stream().forEach(
				v -> {
					//JSONObject varDescriptor = new JSONObject();
					//varDescriptor.put(v.getInstanceName(),v.getValue().toString());
					//descriptor.add(varDescriptor);
					descriptor.put(v.getName(),v.getValue().toString());
				}
				);

		return descriptor;
	}

}
