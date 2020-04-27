package com.dream.core.entities.maps;

import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public interface MotifMap {
	
	public AbstractMotif getOwner();

	public MapNode getNodeForEntity(Entity entity);
	
	public void setEntityMapping(Entity entity,MapNode node);
	
	public void moveEntity(Entity entity,MapNode node);
	
	public boolean existsPath(MapNode node1,MapNode node2);
	
	public boolean isEdge(MapNode node1,MapNode node2);
	
	public Set<Entity> getEntitiesForNode(MapNode node);
	
	public MapNode createNode();
	
	public boolean deleteNode(MapNode node);
	
	public MapEdge createEdge(MapNode node1,MapNode node2);
	
	public boolean deleteEdge(MapNode node1,MapNode node2);

	public boolean hasNode(MapNode node);
	
	public int getNodesSize();
	
	public int getEdgesSize();
	
}
