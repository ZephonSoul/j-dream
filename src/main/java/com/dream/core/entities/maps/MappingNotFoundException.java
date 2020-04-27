package com.dream.core.entities.maps;

import com.dream.core.Entity;

public class MappingNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MappingNotFoundException(MotifMap map, Entity entity) {
		super(getMessage(map,entity));
	}

	private static String getMessage(MotifMap map, Entity entity) {
		return String.format("Referenced entity %s not present in map %s",
				entity.toString(),
				map.toString());
	}

}
