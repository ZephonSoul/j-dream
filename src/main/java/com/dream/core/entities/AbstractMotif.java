/**
 * 
 */
package com.dream.core.entities;

import java.util.HashSet;
import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.coordination.DummyRule;
import com.dream.core.coordination.Rule;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MotifMap;

/**
 * @author Alessandro Maggi
 *
 */
public class AbstractMotif extends AbstractCoordinatingEntity implements CoordinatingEntity {
	
	private MotifMap map;

	/**
	 * @param parent
	 * @param pool
	 * @param rule
	 * @param map
	 */
	public AbstractMotif(Entity parent, Set<Entity> pool, Rule rule, MotifMap map) {
		super(parent, pool, rule);
		this.map = map;
	}

	/**
	 * @param parent
	 * @param pool
	 * @param map
	 */
	public AbstractMotif(Entity parent, Set<Entity> pool, MotifMap map) {
		this(parent, pool, DummyRule.getInstance(), map);
	}

	/**
	 * @param pool
	 * @param rule
	 * @param map
	 */
	public AbstractMotif(Set<Entity> pool, Rule rule, MotifMap map) {
		this(null, pool, rule, map);
	}

	/**
	 * @param pool
	 * @param map
	 */
	public AbstractMotif(Set<Entity> pool, MotifMap map) {
		this(null, pool, DummyRule.getInstance(), map);
	}

	/**
	 * @param parent
	 * @param map
	 */
	public AbstractMotif(Entity parent, MotifMap map) {
		this(parent, new HashSet<>(), DummyRule.getInstance(), map);
	}

	/**
	 * @param map
	 */
	public AbstractMotif(MotifMap map) {
		this(null, new HashSet<>(), DummyRule.getInstance(), map);
	}

	/**
	 * @return the map
	 */
	public MotifMap getMap() {
		return map;
	}

	public MapNode getMapNodeForEntity(Entity entity) {
		return map.getNodeForEntity(entity);
	}
	
	public boolean setEntityPosition(Entity entity,MapNode node) {
		if (pool.contains(entity) && map.hasNode(node)) {
			map.setEntityMapping(entity, node);
			return true;
		} else
			return false;
	}

}
