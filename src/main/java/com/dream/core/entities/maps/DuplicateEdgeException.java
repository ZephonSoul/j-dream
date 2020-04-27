package com.dream.core.entities.maps;

public class DuplicateEdgeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DuplicateEdgeException(MotifMap map,MapEdge edge) {
		super(getMessage(map,edge));
	}

	private static String getMessage(MotifMap map, MapEdge edge) {
		return String.format("Attemt to create edge %s failed: edge already present in map %s",
				edge.toString(),
				map.toString());
	}

}
