package com.dream.core.coordination.constraints.predicates;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;

/**
 * @author Alessandro Maggi
 *
 */
public class SameInstance extends AbstractPredicate implements Predicate {
	
	public final static int BASE_CODE = 3;
	
	List<Instance<?>> instances;
	
	public SameInstance(List<Instance<?>> instances) {
		this.instances = instances;
	}
	
	public SameInstance(Instance<?>... instances) {
		this.instances = Arrays.asList(instances);
	}
	
	public List<Instance<?>> getEntities() {
		return instances;
	}

	@Override
	public boolean sat() {
		boolean equal = true;
		Instance<?> refInstance = null;
		for (Instance<?> instance : instances) {
			if (refInstance == null)
				refInstance = instance;
			else
				equal = equal && refInstance.equals(instance);
			if (!equal)
				return false;
		}
		return true;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof SameInstance)
				&& instances.equals(((SameInstance) formula).getEntities());
	}

	@Override
	public void clearCache() {}

	@Override
	public <I> Predicate bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		if (instances.contains(reference)) {
			List<Instance<?>> instances = 
					this.instances.stream().filter(c -> !c.equals(reference))
					.collect(Collectors.toList());
			instances.add(actual);
			return new SameInstance(instances);
		}
		else
			return this;
	}
	
	public String toString() {
		return "(" + 
				instances.stream().map(Instance::toString).collect(Collectors.joining("="))
				+ ")";
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}

}
