package com.dream.test.coordination;

import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.operations.Skip;

public class DullComponent extends AbstractLightComponent {

	public DullComponent() {
		setRule(new Term(Tautology.getInstance(),Skip.getInstance()));
	}

}
