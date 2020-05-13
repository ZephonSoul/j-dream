/**
 * 
 */
package com.dream.core.entities.maps;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.StoringInstance;
import com.dream.core.localstore.VarStore;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNode implements StoringInstance {

	private MotifMap map;
	private Set<Entity> entities;
	private String name;
	private VarStore store;

	public MapNode(MotifMap map, String name, Set<Entity> entities, VarStore store) {
		this.map = map;
		this.name = name;
		this.entities = entities;
		this.store = store;
		this.store.setOwner(this);
	}

	public MapNode(MotifMap map, String name, Set<Entity> entities) {
		this(map,name,entities,new VarStore());
	}

	public MapNode(MotifMap map, String name) {
		this(map, name, new HashSet<>(),new VarStore());
	}

	public MapNode(String name) {
		this(null, name);
	}

	public boolean hasEntity(Entity entity) {
		return entities.contains(entity);
	}
	
	public Set<Entity> getMappedEntities() {
		return entities;
	}

	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}

	public boolean removeEntity(Entity entity) {
		return entities.remove(entity);
	}

	public MotifMap getMap() {
		return map;
	}

	public void setMap(MotifMap map) {
		this.map = map;
	}

	public VarStore getStore() {
		return store;
	}

	public void setStore(VarStore store) {
		this.store = store;
		this.store.setOwner(this);
	}

	@Override
	public String toString() {
//		if (map != null && map.getOwner() != null)
//			return String.format("%s.%s", map.getOwner().toString(),name);
//		else
			return name;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("name", name);
		descriptor.put("store", store.getJSONDescriptor());

		if (entities != null) {
			JSONArray mappedEntities = new JSONArray();
			entities.stream().forEach(e -> mappedEntities.add(e.toString()));
			descriptor.put("entities", mappedEntities);
		}
		
		return descriptor;
	}

	@Override
	public LocalVariable getVariable(String variableName) {
		return store.getLocalVariable(variableName);
	}

}
