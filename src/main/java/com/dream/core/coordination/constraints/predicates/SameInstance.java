package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.EntityInstance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.Formula;

public class SameInstance extends AbstractPredicate implements Predicate {
	
	public final static int BASE_CODE = 3;
	
	List<EntityInstance> components;
	
	public SameInstance(List<EntityInstance> components) {
		this.components = components;
	}
	
	public SameInstance(EntityInstance... components) {
		this.components = Arrays.asList(components);
	}
	
	public List<EntityInstance> getComponents() {
		return components;
	}

	@Override
	public boolean sat() {
		int id = -1;
		for (EntityInstance c : components) {
			if (!(c instanceof EntityInstanceActual))
				throw new UnboundReferenceException(c);
			if (id == -1)
				id = ((EntityInstanceActual) c).getActualEntity().getId();
			else
				if (id != ((EntityInstanceActual) c).getActualEntity().getId())
					return false;
		}
		return true;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof SameInstance)
				&& components.equals(((SameInstance) formula).getComponents());
	}

	@Override
	public void clearCache() {}

	@Override
	public Formula bindEntityReference(
			EntityInstanceReference componentReference, 
			EntityInstanceActual actualComponent) {
		
		if (components.contains(componentReference)) {
			List<EntityInstance> actualComponents = 
					components.stream().filter(c -> !c.equals(componentReference))
					.collect(Collectors.toList());
			actualComponents.add(actualComponent);
			return new SameInstance(actualComponents);
		}
		else
			return this;
	}
	
	public String toString() {
		return "(" + 
				components.stream().map(EntityInstance::toString).collect(Collectors.joining("="))
				+ ")";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
