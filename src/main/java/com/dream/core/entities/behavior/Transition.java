package com.dream.core.entities.behavior;

import org.json.simple.JSONObject;

import com.dream.core.Caching;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.operations.OperationsSet;
import com.dream.core.coordination.operations.TriggerTransition;

/**
 * @author Alessandro Maggi
 *
 */
public class Transition implements Caching {

	private LTS behavior;
	private ControlLocation source;
	private ControlLocation target;
	private Term rule;
	
	public Transition(
			LTS behavior,
			ControlLocation source, 
			Term rule, 
			ControlLocation target) {
		
		this.behavior = behavior;
		this.source = source;
		this.rule = rule;
		this.target = target;
	}
	
	public Transition(
			ControlLocation source, 
			Term rule, 
			ControlLocation target) {
		
		this(null,source,rule,target);
	}

	/**
	 * @return the source
	 */
	public ControlLocation getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public ControlLocation getTarget() {
		return target;
	}

	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
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
	
	public boolean isActiveForInteraction(Interaction interaction) {
		return rule.sat(interaction);
	}
	
	public OperationsSet getTriggerOperationsSet() {
		return new OperationsSet(new TriggerTransition(this));
	}

	public void trigger() {
		behavior.setCurrentControlLocation(target);
		OperationsSet op = new OperationsSet(rule.getOperation());
		op.executeOperations(false);
	}
	
	public String toString() {
		return String.format("<%s,%s,%s>", 
				source.toString(),
				rule.toString(),
				target.toString());
	}

	@Override
	public void clearCache() {
		rule.clearCache();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject transitionDescriptor = new JSONObject();
		transitionDescriptor.put("rule",rule.toString());
		transitionDescriptor.put("target",target.toString());
		return transitionDescriptor;
	}
	
}