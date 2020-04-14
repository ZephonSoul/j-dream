package com.dream.test.values;

import java.util.Arrays;

import com.dream.core.expressions.values.PrimitiveValue;
import com.dream.core.expressions.values.SetValue;
import com.dream.core.expressions.values.Value;

public class TestValues {

	public static void main(String[] args) {
		Value 	v1 = new PrimitiveValue<>(5),
				v2 = new PrimitiveValue<>(2.5),
				v3 = new PrimitiveValue<>("hello"),
				v4 = new PrimitiveValue<>(5),
				vs1 = new SetValue(v1,v2,v3);
		
		Value[] v = {v1,v2,v3,vs1};
		
		Arrays.stream(v).forEach(val -> System.out.println(val.toString()));
		System.out.println(v1.equals(v4));
		System.out.println(v1.equals(vs1));
	}

}
