package com.dream.core.entities.maps;

/**
 * @author Alessandro Maggi
 *
 */
public class NodeNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NodeNotFoundException(MotifMap map,MapNode node) {
		super(getMessage(map,node));
	}
	
	public NodeNotFoundException(MotifMap map,String matchCriterion) {
		super(getMessage(map,matchCriterion));
	}

	private static String getMessage(MotifMap map, String matchCriterion) {
		return String.format("Map %s does not contain a node satisfying %s", 
				map.toString(), matchCriterion);
	}

	private static String getMessage(MotifMap map, MapNode node) {
		return String.format("Referenced node %s not present in map %s",
				node.toString(),
				map.toString());
	}

}
