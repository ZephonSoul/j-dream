package com.dream.core.expressions.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dream.core.expressions.Expression;

/**
 * @author Alessandro Maggi
 *
 */
public class ArrayValue extends AbstractValue implements Value, AdditiveValue {

	protected List<Value> rawValue;

	public ArrayValue(List<Value> rawValue) {
		this.rawValue = rawValue;
	}

	public ArrayValue(Value... rawElements) {
		this.rawValue = Arrays.stream(rawElements).collect(Collectors.toList());
	}
	
	public ArrayValue(int... rawElements) {
		this.rawValue = Arrays.stream(rawElements).mapToObj(e -> new NumberValue(e)).collect(Collectors.toList());
	}

	public ArrayValue() {
		this.rawValue = new ArrayList<>();
	}

	public List<Value> getRawValue() {
		return rawValue;
	}

	public int getSize() {
		return rawValue.size();
	}

	private void domainCheck(Value value) {
		if (!(value instanceof ArrayValue))
			throw new IncompatibleValueException(value, ArrayValue.class);
	}

	public boolean contains(Value value) {
		return rawValue.contains(value);
	}

	public Value addValue(Value value) {
		List<Value> newRawValue = new ArrayList<>(rawValue);
		newRawValue.add(value);
		return new ArrayValue(newRawValue);
	}

	public Value removeValue(Value v1) {
		return new ArrayValue(
				rawValue.stream().filter(v -> !v.equals(v1))
				.collect(Collectors.toList())
				);
	}

	public Value getValueAt(Value index) {
		if (index instanceof NumberValue) {
			int intIndex = ((NumberValue)index).getRawValue().intValue();
			if (intIndex < rawValue.size())
				return rawValue.get(intIndex);
			else
				throw new ArrayValueIndexOutOfBoundsException(this, intIndex);
		} else {
			throw new IncompatibleValueException(index,NumberValue.class);
		}
	}

	@Override
	public boolean equals(Value value) {
		if (!(value instanceof ArrayValue))
			return false;
		else {
			List<Value> values = ((ArrayValue) value).getRawValue();
			for (int i=0; i<rawValue.size(); i++) {
				if (!rawValue.get(i).equals(values.get(i)))
					return false;
			}
			return true;
		}
	}

	@Override
	public int hashCode() {
		return rawValue.hashCode();
	}

	@Override
	public Value add(AdditiveValue value) {
		domainCheck(value);
		List<Value> newRawValue = new ArrayList<>();
		for (int i=0;i<rawValue.size();i++) {
			newRawValue.add(
					((AdditiveValue)rawValue.get(i)).add(
							(AdditiveValue)(
									((ArrayValue) value).getRawValue().get(i))
							)
					);
		}
		return new ArrayValue(newRawValue);
	}

	@Override
	public Value subtract(AdditiveValue value) {
		domainCheck(value);
		List<Value> newRawValue = new ArrayList<>();
		for (int i=0;i<rawValue.size();i++) {
			newRawValue.add(
					((AdditiveValue)rawValue.get(i)).subtract(
							(AdditiveValue)(
									((ArrayValue) value).getRawValue().get(i))
							)
					);
		}
		return new ArrayValue(newRawValue);
	}

	public String toString() {
		return "[" + 
				rawValue.stream().map(Value::toString)
				.collect(Collectors.joining(","))
				+ "]";
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
		return (ex instanceof ArrayValue)
				&& rawValue.equals(((ArrayValue) ex).getRawValue());
	}

	public int[] toIntArray() {
		if (rawValue.isEmpty())
			return new int[0];
		int[] intArray = new int[rawValue.size()];
		Value[] var = rawValue.toArray(Value[]::new);
		for (int i=0; i<var.length; i++)
			intArray[i] = ((NumberValue)var[i]).getRawValue().intValue();
		return intArray;
	}

}
