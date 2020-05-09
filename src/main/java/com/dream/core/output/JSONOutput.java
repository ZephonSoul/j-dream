/**
 * 
 */
package com.dream.core.output;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Alessandro Maggi
 *
 */
public class JSONOutput {
	
	private JSONObject output;
	private JSONArray states;
	private FileWriter outputFile;

	@SuppressWarnings("unchecked")
	public JSONOutput(String outputPath) {
		output = new JSONObject();
		states = new JSONArray();
		output.put("states",states);
		try {
			outputFile = new FileWriter(outputPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void write(String topic, JSONObject content) {
		if (topic.equals("state"))
			states.add(content);
		else
			output.put(topic, content);
	}
	
	public void close() {
		try {
			outputFile.write(output.toString());
			outputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
