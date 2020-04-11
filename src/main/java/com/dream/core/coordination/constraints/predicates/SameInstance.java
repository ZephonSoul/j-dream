package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.coordination.constraints.Formula;

public class SameInstance extends AbstractPredicate implements Predicate {
	
	public final static int BASE_CODE = 3;
	
	List<ComponentInstance> components;
	
	public SameInstance(List<ComponentInstance> components) {
		this.components = components;
	}
	
	public SameInstance(ComponentInstance... components) {
		this.components = Arrays.asList(components);
	}
	
	public List<ComponentInstance> getComponents() {
		return components;
	}

	@Override
	public boolean sat() {
		int id = -1;
		for (ComponentInstance c : components) {
			if (!(c instanceof ActualComponentInstance))
				throw new UnboundReferenceException(c);
			if (id == -1)
				id = ((ActualComponentInstance) c).getComponent().getId();
			else
				if (id != ((ActualComponentInstance) c).getComponent().getId())
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
	public Formula bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		if (components.contains(componentReference)) {
			List<ComponentInstance> actualComponents = 
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
				components.stream().map(ComponentInstance::toString).collect(Collectors.joining("="))
				+ ")";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
