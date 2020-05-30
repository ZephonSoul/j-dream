/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

	Comparator<Object> ordering;

	/**
	 * @param owner
	 * @param edgeConstructor
	 * @param nodes
	 * @param mapping
	 */
	public ArrayMap(
			AbstractMotif owner,
			Set<MapNode> nodes,
			Map<Entity, MapNode> mapping,
			Comparator<Object> ordering) {

		super(owner, null, nodes, mapping, new HashSet<>());
		this.nodes = nodes;
		properties.put(
				"head", 
				(map) -> {
					NumberValue minIndex = null;
					MapNode head = null;
					for (MapNode node : nodes) {
						NumberValue index = (NumberValue) node.getVariable("index").getValue();
						if (minIndex == null || minIndex.greaterThan(index)) {
							if (!node.getMappedEntities().isEmpty()) {
								minIndex = index;
								head = node;
							}
						}
					}
					return head.getMappedEntities().stream().findFirst().get();
				});
		properties.put(
				"tail", 
				(map) -> {
					NumberValue maxIndex = null;
					MapNode tail = null;
					for (MapNode node : nodes) {
						NumberValue index = (NumberValue) node.getVariable("index").getValue();
						if (maxIndex == null || maxIndex.lessThan(index)) {
							if (!node.getMappedEntities().isEmpty()) {
								maxIndex = index;
								tail = node;
							}
						}
					}
					return tail.getMappedEntities().stream().findFirst().get();
				});
		this.ordering = ordering;
	}

	public ArrayMap(int size,Comparator<Object> ordering) {
		this(null, new HashSet<>(), new HashMap<>(),ordering);
		createNodes(size);
	}

	public MapNode getNodeAtIndex(int index) {
		return getNodeVarEquals("index", new NumberValue(index));
	}

	public MapNode getNodeAtIndex(Value index) {
		NumberValue iValue = (NumberValue) index;
		if (iValue instanceof NumberValue)
			return getNodeVarEquals("index",iValue);
		else
			throw new IncompatibleValueException(index, NumberValue.class);
	}

	public int getIndexForNode(MapNode node) {
		if (!nodes.contains(node))
			throw new NodeNotFoundException(this, node);
		else
			return ((NumberValue) node.getVariable("index").getValue()).getRawValue().intValue();
	}

	//	private void refreshIndexes() {
	//		for (int i=0; i<nodes.size(); i++)
	//			nodes.get(i).getVariable("index").setValue(new NumberValue(i));
	//	}

	@Override
	public boolean isEdge(MapNode node1,MapNode node2) {
		return Math.abs(getIndexForNode(node1)-getIndexForNode(node2))==1;
	}

	@Override
	public boolean deleteNode(MapNode node) {
		boolean removed = super.deleteNode(node);
		removed = nodes.remove(node) & removed;
		//		refreshIndexes();
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

		NumberValue maxIndex = new NumberValue(-1);
		for (MapNode node : nodes) {
			if (!node.equals(newNode)) {
				NumberValue index = (NumberValue) node.getVariable("index").getValue();
				if (index.greaterThan(maxIndex))
					maxIndex = index;
			}
		}
		newNode.getStore().setVarValue("index", maxIndex.add(new NumberValue(1)));

		return newNode;
	}

	@Override
	public MapEdge createEdge(MapNode node1,MapNode node2) {
		return null;
	}

	//	@Override
	//	@SuppressWarnings("unchecked")
	//	public JSONObject getJSONDescriptor() {
	//		JSONObject descriptor = new JSONObject();
	//		descriptor.put("type", this.getClass().getSimpleName());
	//
	//		JSONArray nodesDescriptor = new JSONArray();
	//		for (int i=0; i<nodes.size(); i++) {
	//			JSONObject nDesc = new JSONObject();
	//			nDesc.put(i, getNodeAtIndex(i).getJSONDescriptor());
	//			nodesDescriptor.add(nDesc);
	//		}
	//		//		nodes.stream().forEach(n -> nodesDescriptor.add(n.getJSONDescriptor()));
	//		descriptor.put("nodes", nodesDescriptor);
	//
	//		return descriptor;
	//	}

	private void createNodes(int size) {
		for (int i=0;i<size; i++)
			createNode();
	}

	//	@Override
	//	public MapNode getNodeVarEquals(String varName, Value value) {
	//		if (varName.equals("index") && value instanceof NumberValue) {
	//			int index = ((NumberValue)value).getRawValue().intValue();
	//			if (index >= 0 && index < nodes.size())
	//				return nodes.get(index);
	//			else
	//				return super.getNodeVarEquals(varName,value);
	//		}
	//		else
	//			return super.getNodeVarEquals(varName,value);
	//	}

	@Override
	public void refresh() {
		//TODO: needs work
		if (ordering != null) {
			List<Entity> entities = owner.getPool().stream().collect(Collectors.toList());

			entities.sort(ordering.reversed());
			for (int i=0; i<entities.size(); i++)
				mapping.get(entities.get(i)).getStore().setVarValue("index", new NumberValue(i));
		}
	}

	@Override
	public MapNode getNodeForAddress(Value address) {
		if (address instanceof NumberValue)
			return getNodeAtIndex(address);
		else
			throw new IncompatibleValueException(address, NumberValue.class);
	}

	@Override
	public Value getAddressForNode(MapNode node) {
		return new NumberValue(getIndexForNode(node));

	}

	@Override
	public Value distance(MapNode node1, MapNode node2) {
		NumberValue addr1 = (NumberValue) getAddressForNode(node1);	
		return ((NumberValue) addr1.subtract((NumberValue) getAddressForNode(node2))).getAbsoluteValue();
	}

}
