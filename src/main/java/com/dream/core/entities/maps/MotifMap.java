package com.dream.core.entities.maps;

import java.util.Set;

import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public interface MotifMap {
	
	public AbstractMotif getOwner();
	
	public void setOwner(AbstractMotif owner);
	
	public Set<MapNode> getNodes();

	public MapNode getNodeForEntity(Entity entity);
	
	public void setEntityMapping(Entity entity,MapNode node);
	
	public void removeEntityMapping(Entity entity);
	
	public void moveEntity(Entity entity,MapNode node);
	
	public boolean existsPath(MapNode node1,MapNode node2);
	
	public boolean isEdge(MapNode node1,MapNode node2);
	
	public Value distance(MapNode node1,MapNode node2);
	
	public Set<Entity> getEntitiesForNode(MapNode node);
	
	public MapNode createNode();
	
	public boolean deleteNode(MapNode node);
	
	public MapEdge createEdge(MapNode node1,MapNode node2);
	
	public boolean deleteEdge(MapNode node1,MapNode node2);

	public boolean hasNode(MapNode node);
	
	public int getNodesSize();
	
	public int getEdgesSize();
	
	public MapProperty<?> getProperty(String property);
	
	public JSONObject getJSONDescriptor();

	public MapNode getNodeVarEquals(String varName, Value value);
	
	public default void refresh() {}

	public boolean hasProperty(String property);

	public void addProperty(String propertyName, MapProperty<?> property);

	public MapNode getNodeForAddress(Value address);
	
	public Value getAddressForNode(MapNode node);
	
}
