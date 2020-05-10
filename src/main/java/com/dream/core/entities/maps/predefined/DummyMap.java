/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapEdge;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MapProperty;
import com.dream.core.entities.maps.MappingNotFoundException;
import com.dream.core.entities.maps.MotifMap;
import com.dream.core.entities.maps.NodeNotFoundException;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class DummyMap implements MotifMap {

	private AbstractMotif owner;
	private MapNode node;
	
	/**
	 * @param owner
	 */
	public DummyMap(AbstractMotif owner) {
		this.owner = owner;
		node = new MapNode("o");
	}

	/**
	 * 
	 */
	public DummyMap() {
		this(null);
	}

	@Override
	public AbstractMotif getOwner() {
		return owner;
	}

	@Override
	public void setOwner(AbstractMotif owner) {
		this.owner = owner;
	}
	
	@Override
	public Set<MapNode> getNodes() {
		Set<MapNode> nodes = new HashSet<>();
		nodes.add(node);
		return nodes;
	}

	@Override
	public MapNode getNodeForEntity(Entity entity) {
		if (node.hasEntity(entity))
			return node;
		else
			throw new MappingNotFoundException(this, entity);
	}

	@Override
	public void setEntityMapping(Entity entity, MapNode node) {
		if (this.node.equals(node))
			node.addEntity(entity);
		else
			throw new NodeNotFoundException(this, node);
	}

	@Override
	public void moveEntity(Entity entity, MapNode node) {
		if (!this.node.equals(node))
			throw new NodeNotFoundException(this, node);
		if (!node.hasEntity(entity))
			throw new MappingNotFoundException(this, entity);
	}

	@Override
	public boolean existsPath(MapNode node1, MapNode node2) {
		return false;
	}

	@Override
	public boolean isEdge(MapNode node1, MapNode node2) {
		return false;
	}

	@Override
	public Set<Entity> getEntitiesForNode(MapNode node) {
		if (this.node.equals(node))
			return node.getMappedEntities();
		else
			throw new NodeNotFoundException(this, node);
	}

	@Override
	public MapNode createNode() {
		return null;
	}

	@Override
	public boolean deleteNode(MapNode node) {
		return false;
	}

	@Override
	public MapEdge createEdge(MapNode node1, MapNode node2) {
		return null;
	}

	@Override
	public boolean deleteEdge(MapNode node1, MapNode node2) {
		return false;
	}

	@Override
	public boolean hasNode(MapNode node) {
		return this.node.equals(node);
	}

	@Override
	public int getNodesSize() {
		return 1;
	}

	@Override
	public int getEdgesSize() {
		return 0;
	}

	@Override
	public MapProperty<?> getProperty(String property) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());
		descriptor.put("node", node.getJSONDescriptor());
		
		return descriptor;
	}

	@Override
	public MapNode getNodeVarEquals(String varName, Value value) {
		if (node.getStore().hasLocalVariable(varName) &&
				node.getVariable(varName).getValue().equals(value))
			return node;
		else
			throw new NodeNotFoundException(this, varName + "=" + value.toString());
	}


}
