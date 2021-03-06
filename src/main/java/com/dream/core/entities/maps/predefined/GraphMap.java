/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.AbstractMap;
import com.dream.core.entities.maps.MapEdge;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MotifMap;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class GraphMap extends AbstractMap implements MotifMap {

	/**
	 * @param owner
	 * @param edgeConstructor
	 * @param nodes
	 * @param mapping
	 * @param edges
	 */
	public GraphMap(
			AbstractMotif owner,
			Supplier<? extends MapEdge> edgeConstructor,
			Set<MapNode> nodes, Map<Entity, MapNode> mapping, Set<MapEdge> edges) {
		
		super(owner,edgeConstructor, nodes, mapping, edges);
	}

	/**
	 * @param owner
	 * @param edgeConstructor
	 */
	public GraphMap(AbstractMotif owner, 
			Supplier<? extends MapEdge> edgeConstructor) {
		
		super(owner,edgeConstructor);
		owner.createMapNode();
	}
	
	/**
	 * @param edgeConstructor
	 */
	public GraphMap(Supplier<? extends MapEdge> edgeConstructor) {
		super(null,edgeConstructor);
	}

	@Override
	public MapNode getNodeForAddress(Value address) {
		// No addressing defined
		return null;
	}

	@Override
	public Value getAddressForNode(MapNode node) {
		// No addressing defined
		return null;
	}

	@Override
	public Value distance(MapNode node1, MapNode node2) {
		// TODO Compute number of edges separating nodes
		return null;
	}

}
