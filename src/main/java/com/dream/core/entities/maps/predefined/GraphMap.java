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
	}
	
	/**
	 * @param edgeConstructor
	 */
	public GraphMap(Supplier<? extends MapEdge> edgeConstructor) {
		super(null,edgeConstructor);
	}

}
