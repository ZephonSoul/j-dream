package com.ldream.ldream_core.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SetValue extends AbstractValue implements Value {
	
	protected Set<Value> rawSet;
	
	public SetValue(Set<Value> rawSet) {
		this.rawSet = rawSet;
	}
	
	public SetValue(Value... rawElements) {
		this.rawSet = Arrays.stream(rawElements).collect(Collectors.toSet());
	}
	
	public SetValue() {
		this.rawSet = new HashSet<>();
	}
	
	public Set<Value> getRawSet() {
		return rawSet;
	}

	@Override
	public boolean equals(Value value) {
		return (value instanceof SetValue)
				&& rawSet.equals(((SetValue) value).getRawSet());
	}
	
	@Override
	public int hashCode() {
		return rawSet.hashCode();
	}

	public String toString() {
		return "{" + 
				rawSet.stream().map(Value::toString)
				.collect(Collectors.joining(","))
				+ "}";
	}
	
}
