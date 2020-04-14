package com.dream.core.coordination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Entity;

public class TypeRestriction {
	
	private static final int BASE_CODE = 13;

	private Set<Class<? extends Entity>> types;
	private static TypeRestriction anyTypeInstance;

	@SafeVarargs
	public TypeRestriction(Class<? extends Entity>... type) {
		this.types = Arrays.stream(type).collect(Collectors.toSet());
	}
	
	public TypeRestriction() {
		this.types = new HashSet<>();
	}
	
	public Set<Class<? extends Entity>> getTypes() {
		return types;
	}
	
	public boolean matchAny() {
		return types.size() == 0;
	}

	public boolean match(Entity e) {
		return matchAny() || types.contains(e.getClass());
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
