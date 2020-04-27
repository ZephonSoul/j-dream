/**
 * 
 */
package com.dream.core.entities.maps;

import java.util.HashSet;
import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.entities.Store;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNode {
	
	private MotifMap map;
	private Set<Entity> entities;
	private String name;
	private Store store;
	
	public MapNode(MotifMap map, String name, Set<Entity> entities, Store store) {
		this.map = map;
		this.name = name;
		this.entities = entities;
		this.store = store;
	}
	
	public MapNode(MotifMap map, String name, Set<Entity> entities) {
		this(map,name,entities,new Store());
	}
	
	public MapNode(MotifMap map, String name) {
		this(map, name, new HashSet<>(),new Store());
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
	
	public Store getStore() {
		return store;
	}
	
	public void setStore(Store store) {
		this.store = store;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
