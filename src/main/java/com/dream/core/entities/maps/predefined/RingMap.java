/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.AbstractMap;
import com.dream.core.entities.maps.MapEdge;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MotifMap;

/**
 * @author Alessandro Maggi
 *
 */
public class RingMap extends AbstractMap implements MotifMap {

	private List<MapNode> nodes;

	/**
	 * @param nodes
	 * @param mapping
	 * @param edges
	 */
	public RingMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor,
			Set<MapNode> nodes, 
			Map<Entity, MapNode> mapping, 
			Set<MapEdge> edges) {

		super(owner,edgeConstructor,nodes, mapping, edges);
		this.nodes = nodes.stream().collect(Collectors.toList());
	}

	/**
	 * 
	 */
	public RingMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor) {
		
		super(owner,edgeConstructor);
		this.nodes = new ArrayList<>();
	}

	@Override
	public MapNode createNode()  {
		MapNode newNode = super.createNode();
		if (!nodes.isEmpty()) {
			// Get head and tail nodes of the list
			MapNode head = nodes.get(0),
					tail = nodes.get(nodes.size()-1);
			// Delete connecting edge, and create two new edges
			deleteEdge(tail,head);
			super.createEdge(tail, newNode);
			super.createEdge(newNode, head);
		}
		return newNode;
	}
	
	@Override
	public boolean deleteNode(MapNode node) {
		boolean deleted = super.deleteNode(node);
		if (deleted) {
			// remove connecting edges
			edges.stream().filter(e -> e.connects(node)).forEach(e -> edges.remove(e));
			// find neighbor nodes and reassemble ring
			MapEdge newEdge;
			for (int i=0; i<nodes.size(); i++)
				if (nodes.get(i).equals(node)) {
					newEdge = edgeConstructor.get();
					newEdge.setNodes(nodes.get((i-1) % nodes.size()), node);
					edges.add(newEdge);
					newEdge = edgeConstructor.get();
					newEdge.setNodes(node, nodes.get((i+1) % nodes.size()));
					edges.add(newEdge);
					break;
				}

			// remove node from the list
			nodes.remove(node);
		}
		return deleted;
	}

}
