/**
 * 
 */
package com.dream.core.entities.maps.predefined;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.entities.maps.AbstractMap;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.NodeNotFoundException;
import com.dream.core.expressions.values.ArrayValue;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class GridMap extends AbstractMap {

	protected MapNode[][] nodesGrid;

	public GridMap(MapNode[][] nodesGrid) {
		super();
		this.nodesGrid = nodesGrid;
	}

	public GridMap(int... shape) {
		super();
		//TODO: check shape length
		this.nodesGrid = makeNodesGrid(shape);
	}

	public void setNodesGrid(MapNode[][] nodesGrid) {
		this.nodesGrid = nodesGrid;
	}

	public MapNode[][] makeNodesGrid(int[] shape) {
		MapNode[][] grid = new MapNode[shape[0]][shape[1]];
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[i].length; j++) {
				MapNode n = super.createNode();
				grid[i][j] = n;
			}
		}
		return grid;
	}

	public MapNode[][] makeNodesGrid(ArrayValue shape) {
		return makeNodesGrid(shape.toIntArray());
	}

	@Override
	public Value getAddressForNode(MapNode node) {
		for (int i=0; i<nodesGrid.length; i++) {
			for (int j=0; j<nodesGrid[i].length; j++) {
				if (node.equals(nodesGrid[i][j])) {
					return new ArrayValue(new NumberValue(i),new NumberValue(j));
				}
			}
		}
		throw new NodeNotFoundException(this, node);
	}

	public MapNode getNodeForAddress(int... address) {
		if (address.length == 2) {
			try {
				int x = (nodesGrid.length+address[0]) % nodesGrid.length;
				int y = (nodesGrid[0].length+address[1]) % nodesGrid[1].length;
				return nodesGrid[x][y];
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new NodeNotFoundException(this, String.format("@(%d,%d)", address[0], address[1]));
			}
		} else
			throw new RuntimeException(
					String.format("Wrong address format for GridMap: expected int[2], found int[%d]",address.length));
	}

	@Override
	public MapNode getNodeForAddress(Value address) {
		if (address instanceof ArrayValue) {
			int[] intAddr = ((ArrayValue) address).toIntArray();
			try {
				int x = (nodesGrid.length+intAddr[0]) % nodesGrid.length;
				int y = (nodesGrid[0].length+intAddr[1]) % nodesGrid[1].length;
				return nodesGrid[x][y];
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new NodeNotFoundException(this, String.format("@(%s)", address.toString()));
			}
		} else
			throw new IncompatibleValueException(address, ArrayValue.class);
	}

	@Override
	public boolean isEdge(MapNode node1,MapNode node2) {
		if (!(hasNode(node1)))
			throw new NodeNotFoundException(this, node1);
		if (!(hasNode(node2)))
			throw new NodeNotFoundException(this, node2);
		int[] addr1 = ((ArrayValue) getAddressForNode(node1)).toIntArray();
		int[] addr2 = ((ArrayValue) getAddressForNode(node2)).toIntArray();
		return ((Math.abs(addr1[0]-addr2[0])<=1) && (Math.abs(addr1[1]-addr2[1])<=1));
	}

	@Override
	public boolean deleteNode(MapNode node) {
		// grid map immutable
		return false;
	}

	@Override
	public boolean deleteEdge(MapNode node1,MapNode node2) {
		// grid map immutable
		return false;
	}

	@Override
	public int getEdgesSize() {
		if (nodes.size()==0)
			return 0;
		int n = nodesGrid.length;
		int m = nodesGrid[0].length;
		// compute number of adjacent nodes (also diagonally)
		return (n-1)*m + (m-1)*n + 2*((n-1)*(m-1));
	}

	@Override
	public MapNode createNode() {
		// grid map immutable
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("type", this.getClass().getSimpleName());

		JSONArray nodesDescriptor = new JSONArray();
		nodes.stream().forEach(n -> {
			JSONObject nDesc = n.getJSONDescriptor();
			nDesc.put("address",getAddressForNode(n).toString());
			nodesDescriptor.add(nDesc);
		});
		descriptor.put("nodes", nodesDescriptor);

		return descriptor;
	}

	@Override
	public Value distance(MapNode node1, MapNode node2) {
		int[] addr1 = ((ArrayValue) getAddressForNode(node1)).toIntArray();
		int[] addr2 = ((ArrayValue) getAddressForNode(node2)).toIntArray();
		double distance = Math.sqrt(Math.pow(addr1[0]-addr2[0],2) + Math.pow(addr1[1]-addr2[1],2));
		return new NumberValue(distance);
	}
}
