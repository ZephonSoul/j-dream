/**
 * 
 */
package com.dream.core.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Caching;
import com.dream.core.Entity;
import com.dream.core.AbstractEntity;
import com.dream.core.coordination.Interaction;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.VarStore;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractInteractingEntity 
extends AbstractEntity
implements InteractingEntity, Caching {
	
	final static boolean greedy_cache_update = false;
	
	protected VarStore store;
	protected Map<String,Port> cInterface;
	protected InteractionsIterator interactionsIterator;
	private boolean dirtyCache;

	/**
	 * @param parent
	 * @param store
	 * @param cInterface
	 */
	public AbstractInteractingEntity(Entity parent,VarStore store,Map<String,Port> cInterface) {
		super(parent);
		this.store = store;
		this.cInterface = cInterface;
		setInterfaceOwner();
		store.setOwner(this);
		clearCache();
	}
	
	/**
	 * @param parent
	 * @param store
	 */
	public AbstractInteractingEntity(Entity parent,VarStore store) {
		this(parent,store,new HashMap<>());
	}
	
	/**
	 * @param store
	 * @param cInterface
	 */
	public AbstractInteractingEntity(VarStore store,Map<String,Port> cInterface) {
		this(null,store,cInterface);
	}
	
	/**
	 * @param parent
	 * @param cInterface
	 */
	public AbstractInteractingEntity(Entity parent,Map<String,Port> cInterface) {
		this(parent,new VarStore(),cInterface);
	}
	
	/**
	 * @param parent
	 */
	public AbstractInteractingEntity(Entity parent) {
		this(parent,new VarStore(),new HashMap<>());
	}

	/**
	 * 
	 */
	public AbstractInteractingEntity() {
		this(null,new VarStore(),new HashMap<>());
	}

	/**
	 * @return the store
	 */
	@Override
	public VarStore getStore() {
		return store;
	}
	
	/**
	 * @param variableName
	 * @return the matching LocalVariable
	 */
	@Override
	public LocalVariable getVariable(String variableName) {
		return store.getLocalVariable(variableName);
	}

	/**
	 * @param store the store to set
	 */
	@Override
	public void setStore(VarStore store) {
		this.store = store;
		store.setOwner(this);
	}

	/**
	 * @return the cInterface
	 */
	@Override
	public Map<String,Port> getInterface() {
		return cInterface;
	}

	/**
	 * @param cInterface the cInterface to set
	 */
	@Override
	public void setInterface(Map<String,Port> cInterface) {
		this.cInterface = cInterface;
		setInterfaceOwner();
		clearCache();
	}
	
	/**
	 * @param ports
	 */
	@Override
	public void setInterface(Set<Port> ports) {
		cInterface = new HashMap<>();
		for (Port p : ports) {
			cInterface.put(p.getName(),p);
			p.setOwner(this);
		}
		clearCache();
	}
	
	public void setInterface(Port... ports) {
		setInterface(Arrays.stream(ports).collect(Collectors.toSet()));
	}
	
	@Override
	public Port getPortByName(String name) {
		return cInterface.get(name);
	}
	
	private void setInterfaceOwner() {
		cInterface.values().stream().forEach(p -> p.setOwner(this));
	}
	
//	@Override
//	public int hashCode() {
//		return super.hashCode() 
//				+ store.hashCode() 
//				+ cInterface.values().stream().mapToInt(Port::hashCode).sum();
//	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof AbstractInteractingEntity) &&
				equals((AbstractInteractingEntity) o);
	}

//	public boolean equals(AbstractInteractingEntity entity) {
//		return super.equals(entity) &&
//				store.equals(entity.getStore()) &&
//				cInterface.equals(entity.getInterface());
//	}
	
	@Override
	public Interaction getAllowedInteraction() {
		if (dirtyCache)
			updateCache();
		return interactionsIterator.next();
	}
	
	public boolean isInvolvedInInteraction(Interaction interaction) {
		for (Port p : cInterface.values())
			if (interaction.contains(p))
				return true;
		return false;
	}
	
	protected void updateCache() {
		interactionsIterator = new EntityInteractionsIterator(cInterface.values());
		dirtyCache = false;
	}
	
	@Override
	public void clearCache() {
		if (greedy_cache_update)
			updateCache();
		else {
			dirtyCache = true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {		
		JSONObject descriptor = super.getJSONDescriptor();
		JSONArray interfaceDescriptor = new JSONArray();
		
		descriptor.put("store", store.getJSONDescriptor());
		
		cInterface.values().stream().forEach(p -> interfaceDescriptor.add(p.toString()));
		descriptor.put("interface", interfaceDescriptor);

		return descriptor;
	}

}
