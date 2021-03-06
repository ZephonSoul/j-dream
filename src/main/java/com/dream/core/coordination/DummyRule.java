/**
 * 
 */
package com.dream.core.coordination;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.operations.OperationsSet;
import com.dream.core.operations.Skip;

/**
 * @author Alessandro Maggi
 *
 */
public class DummyRule implements Rule {
	
	public static DummyRule instance;
	
	public static DummyRule getInstance() {
		if (instance == null)
			instance = new DummyRule();
		return instance;
	}

	/**
	 * 
	 */
	public DummyRule() {}

	@Override
	public boolean sat(Interaction i) {
		return true;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction i) {
		return new OperationsSet(Skip.getInstance());
	}

	@Override
	public Rule expandDeclarations() {
		return this;
	}

	@Override
	public boolean equals(Rule rule) {
		return rule instanceof DummyRule;
	}

	@Override
	public void clearCache() {}
	
	@Override
	public String toString() {
		return String.format("(%s -> %s)", Tautology.getInstance().toString(), Skip.getInstance().toString());
	}

	@Override
	public <I> Rule bindInstance(Instance<I> reference, Instance<I> actual) {
		return this;
	}

}
