package com.dream.core.coordination.constraints;

import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
public class And extends AbstractEnnaryFormula implements Formula {
	
	private static final int BASE_CODE = 111;

	public And(Set<Formula> subformulas) {
		super(subformulas);
	}
	
	public And(Formula... subformulas) {
		super(subformulas);
	}

	@Override
	public boolean sat(Interaction i) {
		return subformulas.stream().allMatch(f -> f.sat(i));
	}

	@Override
	public boolean sat() {
		return subformulas.stream().allMatch(f -> f.sat());
	}

//	@Override
//	public Formula bindEntityReference(
//			EntityInstanceRef componentVariable, 
//			EntityInstanceActual actualComponent) {
//
//		return new And(subformulas.stream()
//				.map(f -> f.bindInstance(componentVariable, actualComponent))
//				.collect(Collectors.toSet()));
//	}

	public String getConnectiveSymbol() {
		return "Ʌ";
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof And)
				&& equalSubformulas((And) formula);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		return new And(subformulas.stream()
				.map(f -> f.bindInstance(reference, actual))
				.collect(Collectors.toSet()));
	}

}
