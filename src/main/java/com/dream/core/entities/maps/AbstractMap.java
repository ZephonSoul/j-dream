package com.dream.core.entities.maps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.expressions.values.NumberValue;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractMap implements MotifMap {
	
	protected AbstractMotif owner;
	protected Set<MapNode> nodes;
	protected Map<Entity,MapNode> mapping;
	protected Set<MapEdge> edges;
	protected Map<String,MapProperty<?>> properties;
	protected int nodeCounter;
	
	protected final Supplier<? extends MapEdge> edgeConstructor;
	
	public AbstractMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor,
			Set<MapNode> nodes,
			Map<Entity,MapNode> mapping,
			Set<MapEdge> edges,
			Map<String,MapProperty<?>> properties) {
		
		this.owner = owner;
		this.nodes = nodes;
		setNodesMap();
		this.mapping = mapping;
		this.edges = edges;
		this.properties = properties;
		this.nodeCounter = nodes.size();
		
		this.edgeConstructor = edgeConstructor;
	}
	
	public AbstractMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor,
			Set<MapNode> nodes,
			Map<Entity,MapNode> mapping,
			Set<MapEdge> edges) {
		
		this(
				owner,
				edgeConstructor,
				nodes,
				mapping,
				edges,
				new HashMap<>());
	}

	public AbstractMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor) {
		
		this(
				owner,
				edgeConstructor,
				new HashSet<>(),
				new HashMap<>(),
				new HashSet<>(),
				new HashMap<>()
				);
	}
	
	public AbstractMap(Supplier<? extends MapEdge> edgeConstructor) {
		
		this(
				null,
				edgeConstructor,
				new HashSet<>(),
				new HashMap<>(),
				new HashSet<>(),
				new HashMap<>()
				);
	}
	
	public AbstractMotif getOwner() {
		return owner;
	}
	
	public void setOwner(AbstractMotif owner) {
		this.owner = owner;
	}
	
	public void setProperties(Map<String,MapProperty<?>> properties) {
		this.properties = properties;
	}
	
	public Map<String,MapProperty<?>> getProperties() {
		return properties;
	}
	
	public MapProperty<?> getProperty(String property) {
		return properties.get(property);
	}
	
	public Set<MapNode> getNodes() {
		return nodes;
	}
	
	private void setNodesMap() {
		nodes.stream().forEach(n -> n.setMap(this));
	}
	
	public void setNodes(Set<MapNode> nodes) {
		this.nodes = nodes;
		setNodesMap();
	}
	
	public void setNodes(MapNode... nodes) {
		this.nodes = Arrays.stream(nodes).collect(Collectors.toSet());
		setNodesMap();
	}
	
	public MapNode getNodeForEntity(Entity entity) {
		return mapping.get(entity);
	}
	
	public void setEntityMapping(Entity entity,MapNode node) {
		if (nodes.contains(node)) {
			node.addEntity(entity);
			mapping.put(entity, node);
		} else
			throw new NodeNotFoundException(this, node);
	}
	
	public void moveEntity(Entity entity,MapNode node) {
		if (mapping.containsKey(entity)) {
			if (nodes.contains(node)) {
				mapping.get(entity).removeEntity(entity);
				node.addEntity(entity);
				mapping.put(entity,node);
			} else
				throw new NodeNotFoundException(this, node);
		} else
			throw new MappingNotFoundException(this, entity);
	}
	
	public boolean existsPath(MapNode node1,MapNode node2) {
		//TODO: implement path search algorithm
		return isEdge(node1,node2);
	}
	
	public boolean isEdge(MapNode node1,MapNode node2) {
		for (MapEdge edge : edges)
			if (edge.equals(node1,node2))
				return true;
		return false;
	}
	
	public Set<Entity> getEntitiesForNode(MapNode node) {
		if (nodes.contains(node))
			return node.getMappedEntities();
		else
			throw new NodeNotFoundException(this,node);
	}
	
	public boolean deleteNode(MapNode node) {
		boolean removed = nodes.remove(node);
		if (removed) {
			// remove entities mapped to the node
			for (Entity e : node.getMappedEntities())
				mapping.remove(e);
			// remove incident edges
			//Set<MapEdge> matchingEdges = edges.stream().filter(e -> e.connects(node)).collect(Collectors.toSet());
			//matchingEdges.stream().forEach(e -> edges.remove(e));
			edges.stream().filter(e -> e.connects(node)).forEach(e -> edges.remove(e));
		}
		return removed;
	}
	
	public boolean deleteEdge(MapNode node1,MapNode node2) {
		//edges.stream().filter(e -> e.equals(node1,node2)).forEach(e -> edges.remove(e));
		MapEdge edge = edgeConstructor.get();
		edge.setNodes(node1, node2);
		return edges.remove(edge);
	}
	
	public int getNodesSize() {
		return nodes.size();
	}
	
	public int getEdgesSize() {
		return edges.size();
	}
	
	public MapNode createNode() {
		MapNode newNode = new MapNode(this,String.format("s_%d", nodeCounter));
		nodeCounter++;
		nodes.add(newNode);
		return newNode;
	}
	
	public MapEdge createEdge(MapNode node1,MapNode node2) {
		MapEdge newEdge = edgeConstructor.get();
		newEdge.setNodes(node1, node2);
		if (edges.add(newEdge))
			return newEdge;
		else
			throw new DuplicateEdgeException(this,newEdge);
	}
	
	public boolean hasNode(MapNode node) {
		return nodes.contains(node);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());
		descriptor.put("owner", owner.toString());
		
		JSONArray nodesDescriptor = new JSONArray();
		nodes.stream().forEach(n -> nodesDescriptor.add(n.getJSONDescriptor()));
		descriptor.put("nodes", nodesDescriptor);
		
		JSONArray edgesDescriptor = new JSONArray();
		edges.stream().forEach(e -> edgesDescriptor.add(e.getJSONDescriptor()));
		descriptor.put("edges", edgesDescriptor);
		
		return descriptor;
	}

}
