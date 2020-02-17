package com.ldream.ldream_core.values;

public class NumberValue extends PrimitiveValue<Number> implements AdditiveValue, FactorizableValue, OrderedValue {

	public NumberValue(Number rawValue) {
		super(rawValue);
	}

	private Value roundValue(Number n) {
		if (n.intValue() == (int)n.doubleValue())
			return new NumberValue(n.intValue());
		else
			return new NumberValue(n.doubleValue());
	}

	private void domainCheck(Value value) {
		if (!(value instanceof NumberValue))
			throw new IncompatibleValueException(value, NumberValue.class);
	}

	@Override
	public Value multiplyBy(FactorizableValue value) {
		domainCheck(value);
		return roundValue(rawValue.doubleValue() 
				* ((NumberValue) value).getRawValue().doubleValue());
	}

	@Override
	public Value divideBy(FactorizableValue value) {
		domainCheck(value);
		return roundValue(rawValue.doubleValue() 
				/ ((NumberValue) value).getRawValue().doubleValue());
	}

	@Override
	public Value add(AdditiveValue value) {
		domainCheck(value);
		return roundValue(rawValue.doubleValue() 
					+ ((NumberValue) value).getRawValue().doubleValue());
	}

	@Override
	public Value subtract(AdditiveValue value) {
		domainCheck(value);
		return roundValue(rawValue.doubleValue() 
					- ((NumberValue) value).getRawValue().doubleValue());
	}

	@Override
	public boolean greaterThan(OrderedValue value) {
		domainCheck(value);
		return rawValue.doubleValue() 
				> ((NumberValue) value).getRawValue().doubleValue();
	}

	@Override
	public boolean lessThan(OrderedValue value) {
		domainCheck(value);
		return rawValue.doubleValue() 
				< ((NumberValue) value).getRawValue().doubleValue();
	}

}
