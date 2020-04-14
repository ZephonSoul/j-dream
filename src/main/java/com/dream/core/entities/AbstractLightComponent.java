/**
 * 
 */
package com.dream.core.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.coordination.DummyRule;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Rule;

/**
 * @author alessandro
 *
 */
public class AbstractLightComponent 
extends AbstractCoordinatingEntity
implements CoordinatingEntity, InteractingEntity {

	protected Store store;
	protected Map<String,Port> cInterface;
	protected InteractionsIterator interactionsIterator;
	protected Interaction localInteraction;

	/**
	 * @param parent
	 * @param pool
	 * @param rule
	 */
	public AbstractLightComponent(
			Entity parent, 
			Set<Entity> pool, 
			Rule rule,
			Store store,
			Map<String,Port> cInterface) {

		super(parent, pool, rule);
		this.store = store;
		this.cInterface = cInterface;
		setInterfaceOwner();
		clearCache();
	}

	/**
	 * @param parent
	 * @param pool
	 */
	public AbstractLightComponent(Entity parent, Set<Entity> pool) {
		this(
				parent, 
				pool, 
				DummyRule.getInstance(), 
				new Store(), 
				new HashMap<>()
				);
	}

	/**
	 * @param pool
	 * @param rule
	 */
	public AbstractLightComponent(Set<Entity> pool, Rule rule) {
		this(
				null, 
				pool, 
				rule, 
				new Store(), 
				new HashMap<>()
				);
	}

	/**
	 * @param pool
	 */
	public AbstractLightComponent(Set<Entity> pool) {
		this(
				null, 
				pool, 
				DummyRule.getInstance(), 
				new Store(), 
				new HashMap<>()
				);
	}

	/**
	 * @param parent
	 */
	public AbstractLightComponent(Entity parent) {
		this(
				null, 
				new HashSet<Entity>(), 
				DummyRule.getInstance(), 
				new Store(), 
				new HashMap<>()
				);
	}

	/**
	 * 
	 */
	public AbstractLightComponent() {
		this(
				null, 
				new HashSet<Entity>(), 
				DummyRule.getInstance(), 
				new Store(), 
				new HashMap<>()
				);
	}

	@Override
	public Store getStore() {
		return store;
	}

	@Override
	public void setStore(Store store) {
		this.store = store;
	}
	
	public void setStore(LocalVariable... variables) {
		this.store = new Store(this,variables);
	}

	@Override
	public Map<String, Port> getInterface() {
		return cInterface;
	}

	@Override
	public void setInterface(Map<String, Port> cInterface) {
		this.cInterface = cInterface;
		clearCache();
	}

	@Override
	public void setInterface(Set<Port> ports) {
		cInterface = new HashMap<>();
		for (Port p : ports) {
			cInterface.put(p.getName(),p);
			p.setOwner(this);
		}
		clearCache();
	}
	
	public void setInterface(Port... ports) {
		setInterface(Arrays.stream(ports).collect(Collectors.toSet()));
	}

	private void setInterfaceOwner() {
		cInterface.values().stream().forEach(p -> p.setOwner(this));
	}

	@Override
	public int hashCode() {
		return super.hashCode() 
				+ store.hashCode() 
				+ cInterface.values().stream().mapToInt(Port::hashCode).sum();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof AbstractLightComponent) &&
				equals((AbstractLightComponent) o);
	}

//	public boolean equals(AbstractLightComponent component) {
//		return super.equals(component) &&
//				store.equals(component.getStore()) &&
//				cInterface.equals(component.getInterface());
//	}

	@Override
	public Interaction getAllowedInteraction() {
		if (dirtyCache)
			updateCache();
		Interaction interaction;
		Set<Interaction> forbiddenInteractions = new HashSet<>();
		boolean sat = false;
		do {
			interaction = interactionsIterator.next();
			sat = cached_rule.sat(interaction);
			if (!sat)
				if (forbiddenInteractions.contains(interaction))
					throw new NoAdmissibleInteractionsException(this);
				else
					forbiddenInteractions.add(interaction);
		} while (!sat);
		return interaction;
	}

	protected void updateCache() {
		super.updateCache();
		if (cInterface != null && !cInterface.isEmpty())
			interactionsIterator = 
					new CompoundInteractionsIterator(cInterface.values(),
							poolInteractionsIterator);
		else
			interactionsIterator = poolInteractionsIterator;
		localInteraction = null;
		dirtyCache = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {		
		JSONObject descriptor = super.getJSONDescriptor();
		JSONArray interfaceDescriptor = new JSONArray();

		descriptor.put("store", this.store.toString());

		cInterface.values().stream().forEach(p -> interfaceDescriptor.add(p.toString()));
		descriptor.put("interface", interfaceDescriptor);

		return descriptor;
	}

	@Override
	public Port getPortByName(String portName) {
		return cInterface.get(portName);
	}

}
