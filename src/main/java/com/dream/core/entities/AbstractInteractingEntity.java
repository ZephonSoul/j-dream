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

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractInteractingEntity 
extends AbstractEntity
implements InteractingEntity, Caching {
	
	final static boolean greedy_cache_update = true;
	
	protected Store store;
	protected Map<String,Port> cInterface;
	protected InteractionsIterator interactionsIterator;
	private boolean dirtyCache;

	/**
	 * @param parent
	 * @param store
	 * @param cInterface
	 */
	public AbstractInteractingEntity(Entity parent,Store store,Map<String,Port> cInterface) {
		super(parent);
		this.store = store;
		this.cInterface = cInterface;
		setInterfaceOwner();
		clearCache();
	}
	
	/**
	 * @param parent
	 * @param store
	 */
	public AbstractInteractingEntity(Entity parent,Store store) {
		this(parent,store,new HashMap<>());
	}
	
	/**
	 * @param store
	 * @param cInterface
	 */
	public AbstractInteractingEntity(Store store,Map<String,Port> cInterface) {
		this(null,store,cInterface);
	}
	
	/**
	 * @param parent
	 * @param cInterface
	 */
	public AbstractInteractingEntity(Entity parent,Map<String,Port> cInterface) {
		this(parent,new Store(),cInterface);
	}
	
	/**
	 * @param parent
	 */
	public AbstractInteractingEntity(Entity parent) {
		this(parent,new Store(),new HashMap<>());
	}

	/**
	 * 
	 */
	public AbstractInteractingEntity() {
		this(null,new Store(),new HashMap<>());
	}

	/**
	 * @return the store
	 */
	@Override
	public Store getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	@Override
	public void setStore(Store store) {
		this.store = store;
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
	
	@Override
	public int hashCode() {
		return super.hashCode() 
				+ store.hashCode() 
				+ cInterface.values().stream().mapToInt(Port::hashCode).sum();
	}

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
	
	private void updateCache() {
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
		
		descriptor.put("store", this.store.toString());
		
		cInterface.values().stream().forEach(p -> interfaceDescriptor.add(p.toString()));
		descriptor.put("interface", interfaceDescriptor);

		return descriptor;
	}

}
