/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.entities.maps.MapNode;
import com.dream.core.Instance;

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
	public String getName() {
		return mapNode.toString();
	}

	@Override
	public <I> MapNodeInstance bindInstance(
			Instance<I> reference, Instance<I> actual) {
		
		return this;
	}

}
