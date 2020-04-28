package com.dream.core.expressions.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.expressions.Expression;

/**
 * @author Alessandro Maggi
 *
 */
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
	
	public Value addValue(Value value) {
		Set<Value> newRawSet = new HashSet<>(rawSet);
		newRawSet.add(value);
		return new SetValue(newRawSet);
	}

	public Value removeValue(Value v1) {
		return new SetValue(
				rawSet.stream().filter(v -> !v.equals(v1))
				.collect(Collectors.toSet())
				);
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

	@Override
	public Value eval() {
		return this;
	}

	@Override
	public void evaluateOperands() {}

	@Override
	public void clearCache() {}

	@Override
	public boolean equals(Expression ex) {
		return (ex instanceof SetValue)
				&& equals((SetValue) ex);
	}
	
}
