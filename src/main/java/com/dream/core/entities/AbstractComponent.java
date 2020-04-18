/**
 * 
 */
package com.dream.core.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.operations.OperationsSet;
import com.dream.core.entities.behavior.LTS;

/**
 * @author Alessandro Maggi
 * 
 * This class implements a base DReAM component
 *
 */
public abstract class AbstractComponent 
extends AbstractInteractingEntity 
implements InteractingEntity {

	private LTS behavior;
	
	/**
	 * @param parent
	 * @param store
	 * @param cInterface
	 * @param behavior
	 */
	public AbstractComponent(
			Entity parent, 
			Store store, 
			Map<String, Port> cInterface,
			LTS behavior) {
		
		super(parent, store, cInterface);
		this.behavior = behavior;
	}
	
	/**
	 * @param parent
	 * @param cInterface
	 * @param behavior
	 */
	public AbstractComponent(
			Entity parent, 
			Map<String, Port> cInterface,
			LTS behavior) {
		
		this(parent, new Store(), cInterface, behavior);
	}
	
	/**
	 * @param parent
	 * @param store
	 * @param cInterface
	 * @param behavior
	 */
	public AbstractComponent(
			Entity parent, 
			Store store, 
			LTS behavior) {
		
		this(parent, store, new HashMap<>(),behavior);
	}
	
	public AbstractComponent(Entity parent) {
		super(parent);
	}

	/**
	 * @return the behavior
	 */
	public LTS getBehavior() {
		return behavior;
	}

	/**
	 * @param behavior the behavior to set
	 */
	public void setBehavior(LTS behavior) {
		this.behavior = behavior;
	}
	
	@Override
	public Interaction getAllowedInteraction() {
		boolean sat = false;
		Interaction interaction;
		Set<Interaction> forbiddenInteractions = new HashSet<>();
		do {
			interaction = super.getAllowedInteraction();
			sat = behavior.isInteractionEnabled(interaction);
			if (!sat)
				if (forbiddenInteractions.contains(interaction))
					throw new NoAdmissibleInteractionsException(this);
				else
					forbiddenInteractions.add(interaction);
		} while (!sat);
		return interaction;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		return behavior.getTransitionOperation(interaction);
	}
	
	@Override
	public void updateCache() {
		super.updateCache();
		if (behavior != null)
			behavior.clearCache();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {		
		JSONObject descriptor = super.getJSONDescriptor();

		descriptor.put("behavior", behavior.getJSONDescriptor());

		return descriptor;
	}
	
}
