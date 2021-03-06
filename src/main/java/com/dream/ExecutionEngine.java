package com.dream;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.dream.core.coordination.EntityRefNamesFactory;
import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.NoAdmissibleInteractionsException;
import com.dream.core.exec.ExecutionStrategy;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.operations.Operation;
import com.dream.core.operations.OperationsSet;
import com.dream.core.operations.Skip;
import com.dream.core.output.JSONOutput;
import com.dream.core.output.MessageWritable;
import com.dream.core.output.Output;

/**
 * @author Alessandro Maggi
 *
 */
public class ExecutionEngine implements Runnable {
	
	public final static String version = "0.1.3";
	
	private final static String defaultFileOutput = "ee_log.json";

	private CoordinatingEntity rootEntity;
	private ExecutionStrategy executionStrategy;
	private Output output;
	private JSONOutput jsonOut;
	private boolean interactive;
	private int maxCycles;
	private boolean snapshotSemantics;
	private LocalDateTime launchTimestamp;

	public ExecutionEngine(
			CoordinatingEntity rootEntity,
			ExecutionStrategy executionStrategy,
			Output output,
			boolean interactive,
			int maxCycles,
			String fileOutput) {

		this.rootEntity = rootEntity;
		this.executionStrategy = executionStrategy;
		this.output = output;
		this.interactive = interactive;
		this.maxCycles = maxCycles;
		this.snapshotSemantics = true;
		this.jsonOut = new JSONOutput(fileOutput);
	}
	
	public ExecutionEngine(
			CoordinatingEntity rootEntity,
			ExecutionStrategy executionStrategy,
			Output output,
			boolean interactive,
			int maxCycles) {
		
		this(
				rootEntity,
				GreedyStrategy.getInstance(),
				output,
				interactive,
				maxCycles,
				defaultFileOutput);
	}
	

	public void setSnapshotSemantics(boolean snapshotSemantics) {
		this.snapshotSemantics = snapshotSemantics;
	}

	private OperationsSet executeOperations(OperationsSet ops, boolean snapshotSemantics) {
		if (snapshotSemantics)
			ops.evaluate();
		
		Stack<Operation> scanOps = new Stack<Operation>();
		OperationsSet tempSet = new OperationsSet();
		OperationsSet newOpsSet = ops;
		while (!newOpsSet.isEmpty()) {
			tempSet = newOpsSet;
			newOpsSet = new OperationsSet();
			for (Operation o : tempSet.getOperations()) {
				if (o instanceof OperationsSet)
					newOpsSet.addAllOperationsSet((OperationsSet) o);
				else if (!(o.equals(Skip.getInstance())))
					scanOps.push(o);
			}
		}
		
		// collect flat operations set for output purposes
		tempSet = new OperationsSet(scanOps.stream().collect(Collectors.toSet()));
		if (!snapshotSemantics) {
			Operation o;
			while (!scanOps.empty()) {
				o = scanOps.pop();
				o.evaluate();
				o.execute();
			}
		} else
			while (!scanOps.empty())
				scanOps.pop().execute();
		
		return tempSet;
	}
	
	private LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		descriptor.put("version", version);
		descriptor.put("root", rootEntity.toString());
		descriptor.put("interactive", interactive);
		descriptor.put("max_cycles", maxCycles);
		descriptor.put("snapshot_semantics", snapshotSemantics);
		descriptor.put("execution_strategy", executionStrategy.getClass().getSimpleName());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		descriptor.put("start_time", dtf.format(launchTimestamp));
		
		return descriptor;
	}
	
	private double getElapsed(long startTime) {
		return (System.nanoTime() - startTime)/1000000000.0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		launchTimestamp = getCurrentDateTime();
		jsonOut.write("engine", getJSONDescriptor());
		
		System.out.println("============================");
		System.out.println("DReAM execution engine "+version);
		System.out.println("============================");
		System.out.println(String.format("interactive: %s\nMax cycles: %s\nSnapshot semantics: %s\nExec strategy: %s\n",
				interactive,
				maxCycles,
				snapshotSemantics,
				executionStrategy.getClass().getSimpleName()));

		long startTime = System.nanoTime();
		long initPause = 0;

		int cycles = 0;
		boolean unboundedExecution = false;
		if (maxCycles <= 0)
			unboundedExecution = true;
		
		JSONObject state = new JSONObject();
		state.put("state",rootEntity.getJSONDescriptor());
		state.put("interaction",(new Interaction()).toString());
		state.put("operations",(new OperationsSet()).toString());
		output.write(MessageWritable.write(state));
		jsonOut.write("state", state);
		
		while (cycles < maxCycles || unboundedExecution) {
			try {
				System.out.println("cycle " + (cycles+1) + " (elapsed " + getElapsed(startTime) + ")");
				state = new JSONObject();
				
				output.write(MessageWritable.write("Expanded rule:\n",rootEntity.getExpandedRule()));
				
				Interaction interaction = executionStrategy.selectInteraction(rootEntity);
				OperationsSet opsSet = rootEntity.getOperationsForInteraction(interaction);

				output.write(MessageWritable.write("Performed interaction = ",interaction));

//				opsSet.executeOperations(snapshotSemantics);
				opsSet = executeOperations(opsSet,snapshotSemantics);
				output.write(MessageWritable.write("Performed operations = ",opsSet));
				interaction.trigger();
//				output.write(MessageWritable.write("Performed interaction = ",interaction));
				state.put("interaction",interaction.toString());
				state.put("operations",opsSet.toString());
				state.put("state", rootEntity.getJSONDescriptor());
				jsonOut.write("state", state);

				output.write(MessageWritable.write(rootEntity.getJSONDescriptor()));
				cycles++;
				if (interactive) {
					initPause = System.nanoTime();
					System.out.println("Press ENTER to continue");
					try {
						System.in.read();
						startTime += (System.nanoTime() - initPause);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rootEntity.clearCache();
				EntityRefNamesFactory.getInstance().reset();
			} catch (NoAdmissibleInteractionsException e) {
				output.write(MessageWritable.write(e.getMessage()));
				break;
			}
		}

		long stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime)/1000000000.0;

		System.out.println("============================");
		System.out.println(String.format("Completed cycles: %s\nExecution time: %s s",
				cycles,
				elapsedTime));
		
		jsonOut.close();
	}

}
