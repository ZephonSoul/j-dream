package com.dream.core.coordination;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.components.Component;

@SuppressWarnings("rawtypes")
public class TypeRestriction {
	
	private static final int BASE_CODE = 13;

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
		return (types.size() == type.getTypes().size())
				&& types.containsAll(type.getTypes());
	}
	
	public static TypeRestriction anyType() {
		if (anyTypeInstance == null)
			anyTypeInstance = new TypeRestriction();
		return anyTypeInstance;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof TypeRestriction)
				&& equals((TypeRestriction) o);
	}
	
	@Override
	public int hashCode() {
		if (matchAny())
			return BASE_CODE;
		else
			return types.stream().mapToInt(Class::hashCode).sum();
	}

}
