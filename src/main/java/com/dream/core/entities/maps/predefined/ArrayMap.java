/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.Entity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.AbstractMap;
import com.dream.core.entities.maps.MapEdge;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.NodeNotFoundException;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class ArrayMap extends AbstractMap {

	ArrayList<MapNode> nodes;

	/**
	 * @param owner
	 * @param edgeConstructor
	 * @param nodes
	 * @param mapping
	 */
	public ArrayMap(
			AbstractMotif owner,
			ArrayList<MapNode> nodes,
			Map<Entity, MapNode> mapping) {

		super(owner, null, nodes.stream().collect(Collectors.toSet()), mapping, null);
		this.nodes = nodes;
		properties.put(
				"head", 
				() -> {
					return nodes.get(0).getMappedEntities().stream().findFirst().get();
				});
		properties.put(
				"tail", 
				() -> {
					for (int i=nodes.size()-1; i>=0; i--) {
						MapNode n = nodes.get(i);
						if (!n.getMappedEntities().isEmpty())
							return n.getMappedEntities().stream().findFirst().get();
					}
					return null;
				});
	}

	public ArrayMap(int size) {
		this(null, new ArrayList<>(), new HashMap<>());
		createNodes(size);
	}

	public MapNode getNodeAtIndex(int index) {
		return nodes.get(index);
	}

	public MapNode getNodeAtIndex(Value index) {
		NumberValue iValue = (NumberValue) index;
		if (iValue instanceof NumberValue)
			return nodes.get(iValue.getRawValue().intValue());
		else
			throw new IncompatibleValueException(index, NumberValue.class);
	}

	public int getIndexForNode(MapNode node) {
		int index = nodes.indexOf(node);
		if (index == -1)
			throw new NodeNotFoundException(this, node);
		else
			return index;
	}

	@Override
	public boolean isEdge(MapNode node1,MapNode node2) {
		return Math.abs(getIndexForNode(node1)-getIndexForNode(node2))==1;
	}

	@Override
	public boolean deleteNode(MapNode node) {
		boolean removed = super.deleteNode(node);
		removed = nodes.remove(node) & removed;
		return removed;
	}

	@Override
	public boolean deleteEdge(MapNode node1,MapNode node2) {
		return false;
	}

	@Override
	public int getEdgesSize() {
		return nodes.size()-1;
	}

	@Override
	public MapNode createNode() {
		MapNode newNode = super.createNode();
		int index = nodes.size();
		newNode.getStore().setVarValue("index", new NumberValue(index));
		nodes.add(newNode);
		return newNode;
	}

	@Override
	public MapEdge createEdge(MapNode node1,MapNode node2) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());
		descriptor.put("owner", owner.toString());

		JSONArray nodesDescriptor = new JSONArray();
		for (int i=0; i<nodes.size(); i++) {
			JSONObject nDesc = new JSONObject();
			nDesc.put(i, nodes.get(i).getJSONDescriptor());
			nodesDescriptor.add(nDesc);
		}
		//		nodes.stream().forEach(n -> nodesDescriptor.add(n.getJSONDescriptor()));
		descriptor.put("nodes", nodesDescriptor);

		return descriptor;
	}

	private void createNodes(int size) {
		for (int i=0;i<size; i++)
			createNode();
	}
	
	@Override
	public MapNode getNodeVarEquals(String varName, Value value) {
		if (varName.equals("index") && value instanceof NumberValue)
			return nodes.get(((NumberValue)value).getRawValue().intValue());
		else
			return super.getNodeVarEquals(varName,value);
	}

}
