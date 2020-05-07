/**
 * 
 */
package com.dream.core.entities.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Caching;
import com.dream.core.coordination.Interaction;
import com.dream.core.operations.OperationsSet;

/**
 * @author Alessandro Maggi
 *
 */
public class LTS implements Caching {

	private Map<ControlLocation,Set<Transition>> transitions;
	private ControlLocation currentControlLocation;
	private Map<Interaction,Transition> cached_transitions;

	public LTS(
			Map<ControlLocation,Set<Transition>> transitions,
			ControlLocation currentControlLocation) {
		
		this.transitions = transitions;
		this.currentControlLocation = currentControlLocation;
		this.cached_transitions = new HashMap<>();
		bindTransitions();
	}

	/**
	 * @return the currentControlLocation
	 */
	public ControlLocation getCurrentControlLocation() {
		return currentControlLocation;
	}
	
	public void setCurrentControlLocation(ControlLocation controlLocation) {
		currentControlLocation = controlLocation;
	}

	/**
	 * @return the transitions
	 */
	public Map<ControlLocation, Set<Transition>> getTransitions() {
		return transitions;
	}
	
	private void bindTransitions() {
		transitions.values().stream().forEach(
				ts -> ts.stream().forEach(
						t -> t.setBehavior(this)));
	}

	public boolean isInteractionEnabled(Interaction interaction) {
		for (Transition t : transitions.get(currentControlLocation))
			if (t.isActiveForInteraction(interaction)) {
				cached_transitions.put(interaction, t);
				return true;
			}
		return false;
	}

	@Override
	public void clearCache() {
		cached_transitions.values().stream().forEach(Transition::clearCache);
	}

	public OperationsSet getTransitionOperation(Interaction interaction) {
		for (Interaction i : cached_transitions.keySet())
			if (i.subsetOf(interaction))
				return cached_transitions.get(i).getTriggerOperationsSet();
		return new OperationsSet();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject ltsDescriptor = new JSONObject();
		ltsDescriptor.put("current control location", currentControlLocation.toString());
		
		JSONArray transitionsDescriptor = new JSONArray();
		
		for (ControlLocation cLoc : transitions.keySet()) {
			JSONArray tDesc = new JSONArray();
			for (Transition t : transitions.get(cLoc)) {
				tDesc.add(t.getJSONDescriptor());
			}
			JSONObject ct = new JSONObject();
			ct.put(cLoc.toString(), tDesc);
			transitionsDescriptor.add(ct);
		}
		ltsDescriptor.put("transitions", transitionsDescriptor);
		return ltsDescriptor;
	}

}
