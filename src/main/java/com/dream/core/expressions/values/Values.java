/**
 * 
 */
package com.dream.core.expressions.values;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessandro Maggi
 *
 */
public class Values {
	
	public static ArrayValue newArrayValueRange(int start, int stop, int step) {
		List<Value> values = new ArrayList<>();
		int v = start;
		while (v < stop) {
			values.add(new NumberValue(v));
			v += step;
		}
		return new ArrayValue(values);
	}

	public static ArrayValue newArrayValueRange(NumberValue start, NumberValue stop, NumberValue step) {
		List<Value> values = new ArrayList<>();
		NumberValue v = start;
		while (v.lessThan(stop)) {
			values.add(v);
			v = (NumberValue) v.add(step);
		}
		return new ArrayValue(values);
	}

}
