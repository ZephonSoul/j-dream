/**
 * 
 */
package com.dream.core.entities.maps.predefined;


import com.dream.core.entities.maps.AbstractMapEdge;
import com.dream.core.entities.maps.MapEdge;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class UndirectedMapEdge extends AbstractMapEdge implements MapEdge {

	/**
	 * @param node1
	 * @param node2
	 */
	public UndirectedMapEdge(MapNode node1, MapNode node2) {
		super(node1, node2);
	}
	
	public UndirectedMapEdge() {
		super();
	}

	@Override
	public boolean equals(MapNode node1, MapNode node2) {
		return (this.node1.equals(node1) && this.node2.equals(node2)) ||
				(this.node1.equals(node2) && this.node2.equals(node1));
	}
	
	@Override
	public int hashCode() {
		return node1.hashCode() + node2.hashCode();
	}
	
	@Override
	public boolean equals(MapEdge edge) {
		return (edge instanceof UndirectedMapEdge) &&
				equals(
						((UndirectedMapEdge)edge).getNode1(),
						((UndirectedMapEdge)edge).getNode2()
						);
	}

}
