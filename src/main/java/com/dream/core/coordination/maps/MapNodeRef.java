/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.Instance;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeRef implements MapNodeInstance {

	final static int BASE_CODE = 211;

	private String name;

	public MapNodeRef() {
		this.name = MapNodeRefNamesFactory.getInstance().getFreshName();
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return BASE_CODE + name.hashCode();
	}

	public boolean equals(MapNodeRef mapNodeRef) {
		return name.equals(mapNodeRef.getName());
	}

	@Override
	public MapNode getActual() {
		return null;
	}

	@Override
	public <I> MapNodeInstance bindInstance(Instance<I> reference, Instance<I> actual) {
		if (this.equals(reference))
			return (MapNodeInstance) actual;
		else
			return this;
	}

}
