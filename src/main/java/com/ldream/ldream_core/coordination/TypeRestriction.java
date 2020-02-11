package com.ldream.ldream_core.coordination;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;

@SuppressWarnings("rawtypes")
public class TypeRestriction {

	private Set<Class> types;

	public TypeRestriction(Class... type) {
		this.types = Arrays.asList(type).stream().collect(Collectors.toSet());
	}
	
	public Set<Class> getTypes() {
		return types;
	}

	public boolean match(Component c) {
		if (types.size() == 0)
			return true;
		else
			return types.contains(c.getClass());
	}

	public String toString() {
		String result = String.join(",", 
				types.stream().map(Class::getSimpleName).toArray(String[]::new));
		switch(types.size()) {
		case 0: return "*";
		case 1: return result;
		default: return "{" + result + "}";
		}
	}
	
	public boolean equals(TypeRestriction type) {
		if (types.size() == type.getTypes().size())
			return types.containsAll(type.getTypes());
		else
			return false;
	}

}
