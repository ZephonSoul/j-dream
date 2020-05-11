/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	Comparator<Object> ordering;

	/**
	 * @param owner
	 * @param edgeConstructor
	 * @param nodes
	 * @param mapping
	 */
	public ArrayMap(
			AbstractMotif owner,
			ArrayList<MapNode> nodes,
			Map<Entity, MapNode> mapping,
			Comparator<Object> ordering) {

		super(owner, null, nodes.stream().collect(Collectors.toSet()), mapping, new HashSet<>());
		this.nodes = nodes;
		properties.put(
				"head", 
				() -> {
					MapNode n;
					for (int i=0; i<nodes.size(); i++) {
						n = nodes.get(i);
						if (!n.getMappedEntities().isEmpty())
							return n.getMappedEntities().stream().findFirst().get();
					}
					return null;
				});
		properties.put(
				"tail", 
				() -> {
					MapNode n;
					for (int i=nodes.size()-1; i>=0; i--) {
						 n = nodes.get(i);
						if (!n.getMappedEntities().isEmpty())
							return n.getMappedEntities().stream().findFirst().get();
					}
					return null;
				});
		this.ordering = ordering;
	}

	public ArrayMap(int size,Comparator<Object> ordering) {
		this(null, new ArrayList<>(), new HashMap<>(),ordering);
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
	
	private void refreshIndexes() {
		for (int i=0; i<nodes.size(); i++)
			nodes.get(i).getVariable("index").setValue(new NumberValue(i));
	}

	@Override
	public boolean isEdge(MapNode node1,MapNode node2) {
		return Math.abs(getIndexForNode(node1)-getIndexForNode(node2))==1;
	}

	@Override
	public boolean deleteNode(MapNode node) {
		boolean removed = super.deleteNode(node);
		removed = nodes.remove(node) & removed;
		refreshIndexes();
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
	
	@Override
	public void refresh() {
		List<Entity> entities = owner.getPool().stream().collect(Collectors.toList());
		
		// Compensate distortions: more entities than MapNodes
		int distortion = entities.size() - nodes.size();
		if (distortion > 0)
			for (int i=0; i<distortion; i++)
				owner.createMapNode();
		
		entities.sort(ordering.reversed());
		for (int i=0; i<entities.size(); i++) {
			moveEntity(entities.get(i), nodes.get(i));
		}
	}

}
