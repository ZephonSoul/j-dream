/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeActual implements MapNodeInstance {
	
	private MapNode mapNode;
	
	public MapNodeActual(MapNode mapNode) {
		this.mapNode = mapNode;
	}

	@Override
	public MapNode getActual() {
		return mapNode;
	}

	@Override
	public String toString() {
		return mapNode.toString();
	}

	@Override
	public void evaluate() {}

}
