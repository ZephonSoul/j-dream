package com.ldream.ldream_core.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SetValue extends AbstractValue implements Value, AdditiveValue {
	
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
	
	public int getSize() {
		return rawSet.size();
	}
	
	public boolean isSubsetOf(SetValue superset) {
		return superset.getRawSet().containsAll(rawSet);
	}
	
	public boolean isSupersetOf(SetValue subset) {
		return rawSet.containsAll(subset.getRawSet());
	}
	
	private void domainCheck(Value value) {
		if (!(value instanceof SetValue))
			throw new IncompatibleValueException(value, SetValue.class);
	}

	public boolean contains(Value value) {
		return rawSet.contains(value);
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

	@Override
	public Value add(AdditiveValue value) {
		domainCheck(value);
		Set<Value> newRawSet = new HashSet<>(rawSet);
		newRawSet.addAll(((SetValue) value).getRawSet());
		return new SetValue(newRawSet);
	}

	@Override
	public Value subtract(AdditiveValue value) {
		domainCheck(value);
		Set<Value> rawSetFilter = ((SetValue) value).getRawSet();
		return new SetValue(
				rawSet.stream().filter(v -> !rawSetFilter.contains(v))
				.collect(Collectors.toSet()));
	}

	public String toString() {
		return "{" + 
				rawSet.stream().map(Value::toString)
				.collect(Collectors.joining(","))
				+ "}";
	}
	
}
