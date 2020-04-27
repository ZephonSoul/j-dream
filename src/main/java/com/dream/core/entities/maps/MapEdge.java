package com.dream.core.entities.maps;

public interface MapEdge {

	public boolean equals(MapNode node1, MapNode node2);
	
	public boolean connects(MapNode node);

	public void setNodes(MapNode node1, MapNode node2);
	
	public boolean equals(MapEdge edge);

}
