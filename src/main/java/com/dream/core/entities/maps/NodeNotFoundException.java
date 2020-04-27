package com.dream.core.entities.maps;

public class NodeNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NodeNotFoundException(MotifMap map,MapNode node) {
		super(getMessage(map,node));
	}

	private static String getMessage(MotifMap map, MapNode node) {
		return String.format("Referenced node %s not present in map %s",
				node.toString(),
				map.toString());
	}

}
