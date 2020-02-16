package com.ldream.ldream_core.coordination;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;

@SuppressWarnings("rawtypes")
public class TypeRestriction {

	private Set<Class> types;
	private static TypeRestriction anyTypeInstance;

	public TypeRestriction(Class... type) {
		this.types = Arrays.asList(type).stream().collect(Collectors.toSet());
	}
	
	public Set<Class> getTypes() {
		return types;
	}
	
	public boolean matchAny() {
		return types.size() == 0;
	}

	public boolean match(Component c) {
		return matchAny() || types.contains(c.getClass());
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
	
	public static TypeRestriction anyType() {
		if (anyTypeInstance == null)
			anyTypeInstance = new TypeRestriction();
		return anyTypeInstance;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TypeRestriction)
			return equals((TypeRestriction) o);
		else
			return false;
	}

}
