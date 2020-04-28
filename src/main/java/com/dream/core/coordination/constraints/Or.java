package com.dream.core.coordination.constraints;

import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
public class Or extends AbstractEnnaryFormula implements Formula {
	
	private static final int BASE_CODE = 7;

	public Or(Set<Formula> subformulas) {
		super(subformulas);
	}
	
	public Or(Formula... subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {
		return subformulas.stream().anyMatch(f -> f.sat(i));
	}

	@Override
	public boolean sat() {
		return subformulas.stream().anyMatch(f -> f.sat());
	}

//	@Override
//	public Formula bindEntityReference(
//			EntityInstanceRef componentVariable, 
//			EntityInstanceActual actualComponent) {
//
//		return new Or(subformulas.stream()
//				.map(f -> f.bindInstance(componentVariable, actualComponent))
//				.collect(Collectors.toSet()));
//	}

	public String getConnectiveSymbol() {
		return "V";
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Or)
				&& equalSubformulas((Or) formula);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		return new Or(subformulas.stream()
				.map(f -> f.bindInstance(reference, actual))
				.collect(Collectors.toSet()));
	}
}
