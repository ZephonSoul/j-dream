package com.dream.core.entities.maps;

import org.json.simple.JSONObject;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractMapEdge implements MapEdge {
	
	protected MapNode node1;
	protected MapNode node2;
	
	public AbstractMapEdge() {}

	public AbstractMapEdge(MapNode node1,MapNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	/**
	 * @return the node1
	 */
	public MapNode getNode1() {
		return node1;
	}

	/**
	 * @param node1 the node1 to set
	 */
	public void setNode1(MapNode node1) {
		this.node1 = node1;
	}

	/**
	 * @return the node2
	 */
	public MapNode getNode2() {
		return node2;
	}

	/**
	 * @param node2 the node2 to set
	 */
	public void setNode2(MapNode node2) {
		this.node2 = node2;
	}
	
	@Override
	public int hashCode() {
		return node1.hashCode() - node2.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MapEdge)
			return equals((MapEdge)o);
		else
			return false;
	}
	
	@Override
	public String toString() {
		return String.format("<%s,%s>", node1.toString(),node2.toString());
	}
	
	@Override
	public boolean equals(MapEdge edge) {
		return (edge instanceof AbstractMapEdge) &&
				node1.equals(((AbstractMapEdge) edge).getNode1()) &&
				node2.equals(((AbstractMapEdge) edge).getNode2());
	}

	@Override
	public boolean connects(MapNode node) {
		return node1.equals(node) || node2.equals(node);
	}

	@Override
	public void setNodes(MapNode node1, MapNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());
		descriptor.put("node_1", node1.toString());
		descriptor.put("node_2", node2.toString());
		
		return descriptor;
	}
	
}
