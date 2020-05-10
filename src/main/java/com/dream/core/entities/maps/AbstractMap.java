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
import com.dream.core.expressions.values.Value;

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
	
	@Override
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
	
	@Override
	public MapNode getNodeForEntity(Entity entity) {
		return mapping.get(entity);
	}
	
	@Override
	public void setEntityMapping(Entity entity,MapNode node) {
		if (nodes.contains(node)) {
			node.addEntity(entity);
			mapping.put(entity, node);
		} else
			throw new NodeNotFoundException(this, node);
	}
	
	@Override
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
	
	@Override
	public boolean existsPath(MapNode node1,MapNode node2) {
		//TODO: implement path search algorithm
		return isEdge(node1,node2);
	}
	
	@Override
	public boolean isEdge(MapNode node1,MapNode node2) {
		for (MapEdge edge : edges)
			if (edge.equals(node1,node2))
				return true;
		return false;
	}
	
	@Override
	public Set<Entity> getEntitiesForNode(MapNode node) {
		if (nodes.contains(node))
			return node.getMappedEntities();
		else
			throw new NodeNotFoundException(this,node);
	}
	
	@Override
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
	
	@Override
	public boolean deleteEdge(MapNode node1,MapNode node2) {
		//edges.stream().filter(e -> e.equals(node1,node2)).forEach(e -> edges.remove(e));
		MapEdge edge = edgeConstructor.get();
		edge.setNodes(node1, node2);
		return edges.remove(edge);
	}
	
	@Override
	public int getNodesSize() {
		return nodes.size();
	}
	
	@Override
	public int getEdgesSize() {
		return edges.size();
	}
	
	@Override
	public MapNode createNode() {
		MapNode newNode = new MapNode(this,String.format("n_%d", nodeCounter));
		nodeCounter++;
		nodes.add(newNode);
		return newNode;
	}
	
	@Override
	public MapEdge createEdge(MapNode node1,MapNode node2) {
		MapEdge newEdge = edgeConstructor.get();
		newEdge.setNodes(node1, node2);
		if (edges.add(newEdge))
			return newEdge;
		else
			throw new DuplicateEdgeException(this,newEdge);
	}
	
	@Override
	public boolean hasNode(MapNode node) {
		return nodes.contains(node);
	}
	
	@Override
	public MapNode getNodeVarEquals(String varName, Value value) {
		for (MapNode n : nodes) {
			if (n.getStore().hasLocalVariable(varName) &&
					n.getStore().getLocalVariable(varName).getValue().equals(value))
				return n;
		}
		throw new NodeNotFoundException(this, varName + "=" + value.toString());
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());
		
		JSONArray nodesDescriptor = new JSONArray();
		nodes.stream().forEach(n -> nodesDescriptor.add(n.getJSONDescriptor()));
		descriptor.put("nodes", nodesDescriptor);
		
		JSONArray edgesDescriptor = new JSONArray();
		edges.stream().forEach(e -> edgesDescriptor.add(e.getJSONDescriptor()));
		descriptor.put("edges", edgesDescriptor);
		
		return descriptor;
	}

}
