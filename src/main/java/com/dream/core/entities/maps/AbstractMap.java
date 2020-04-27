package com.dream.core.entities.maps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractMap implements MotifMap {
	
	protected AbstractMotif owner;
	protected Set<MapNode> nodes;
	protected Map<Entity,MapNode> mapping;
	protected Set<MapEdge> edges;
	protected int nodeCounter;
	
	protected final Supplier<? extends MapEdge> edgeConstructor;
	
	public AbstractMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor,
			Set<MapNode> nodes,
			Map<Entity,MapNode> mapping,
			Set<MapEdge> edges) {
		
		this.owner = owner;
		this.nodes = nodes;
		this.mapping = mapping;
		this.edges = edges;
		this.nodeCounter = nodes.size();
		
		this.edgeConstructor = Objects.requireNonNull(edgeConstructor);
	}

	public AbstractMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor) {
		
		this(
				owner,
				edgeConstructor,
				new HashSet<>(),
				new HashMap<>(),
				new HashSet<>()
				);
	}
	
	public AbstractMotif getOwner() {
		return owner;
	}
	
	public MapNode getNodeForEntity(Entity entity) {
		return mapping.get(entity);
	};
	
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
	};
	
	public boolean deleteEdge(MapNode node1,MapNode node2) {
		//edges.stream().filter(e -> e.equals(node1,node2)).forEach(e -> edges.remove(e));
		MapEdge edge = edgeConstructor.get();
		edge.setNodes(node1, node2);
		return edges.remove(edge);
	};
	
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
	};
	
	public MapEdge createEdge(MapNode node1,MapNode node2) {
		MapEdge newEdge = edgeConstructor.get();
		newEdge.setNodes(node1, node2);
		if (edges.add(newEdge))
			return newEdge;
		else
			throw new DuplicateEdgeException(this,newEdge);
	};
	
	public boolean hasNode(MapNode node) {
		return nodes.contains(node);
	}

}
