package com.dream;

import java.io.IOException;
import java.util.Stack;
import java.util.stream.Collectors;

import com.dream.core.coordination.Interaction;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.NoAdmissibleInteractionsException;
import com.dream.core.exec.ExecutionStrategy;
import com.dream.core.operations.Operation;
import com.dream.core.operations.OperationsSet;
import com.dream.core.output.MessageWritable;
import com.dream.core.output.Output;

/**
 * @author Alessandro Maggi
 *
 */
public class ExecutionEngine implements Runnable {

	private CoordinatingEntity rootEntity;
	private ExecutionStrategy executionStrategy;
	private Output output;
	private boolean interactive;
	private int maxCycles;
	private boolean snapshotSemantics;

	public ExecutionEngine(
			CoordinatingEntity rootEntity,
			ExecutionStrategy executionStrategy,
			Output output) {

		this.rootEntity = rootEntity;
		this.executionStrategy = executionStrategy;
		this.output = output;
		this.maxCycles = 0;
		this.snapshotSemantics = true;
	}

	public ExecutionEngine(
			CoordinatingEntity rootComponent,
			ExecutionStrategy executionStrategy,
			Output output,
			boolean interactive) {

		this(rootComponent,executionStrategy,output);
		this.interactive = interactive;
	}

	public ExecutionEngine(
			CoordinatingEntity rootComponent,
			ExecutionStrategy executionStrategy,
			Output output,
			int maxCycles) {

		this(rootComponent,executionStrategy,output,false);
		this.maxCycles = maxCycles;
	}

	public ExecutionEngine(
			CoordinatingEntity rootComponent,
			ExecutionStrategy executionStrategy,
			Output output,
			boolean interactive,
			int maxCycles) {

		this(rootComponent,executionStrategy,output);
		this.maxCycles = maxCycles;
		this.interactive = interactive;
	}

	public void setSnapshotSemantics(boolean snapshotSemantics) {
		this.snapshotSemantics = snapshotSemantics;
	}

	private OperationsSet executeOperations(OperationsSet ops, boolean snapshotSemantics) {
		if (snapshotSemantics)
			ops.evaluateOperands();
		
		Stack<Operation> scanOps = new Stack<Operation>();
		OperationsSet tempSet = new OperationsSet();
		OperationsSet newOpsSet = ops;
		while (!newOpsSet.isEmpty()) {
			tempSet = newOpsSet;
			newOpsSet = new OperationsSet();
			for (Operation o : tempSet.getOperations()) {
				if (o instanceof OperationsSet)
					newOpsSet.addAllOperationsSet((OperationsSet) o);
				else
					scanOps.push(o);
			}
		}
		
		// collect flat operations set for output purposes
		tempSet = new OperationsSet(scanOps.stream().collect(Collectors.toSet()));
		if (!snapshotSemantics) {
			Operation o;
			while (!scanOps.empty()) {
				o = scanOps.pop();
				o.evaluateOperands();
				o.execute();
			}
		} else
			while (!scanOps.empty())
				scanOps.pop().execute();
		
		return tempSet;
	}

	@Override
	public void run() {
		System.out.println("============================");
		System.out.println("DReAM execution engine 0.1.1");
		System.out.println("============================");
		System.out.println(String.format("interactive: %s\nMax cycles: %s\nSnapshot semantics: %s\nExec strategy: %s",
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
		output.write(MessageWritable.write(rootEntity.getJSONDescriptor()));
		while (cycles < maxCycles || unboundedExecution) {
			try {
				Interaction interaction = executionStrategy.selectInteraction(rootEntity);
				OperationsSet opsSet = rootEntity.getOperationsForInteraction(interaction);

				output.write(MessageWritable.write("Expanded rule:\n",rootEntity.getExpandedRule()));

//				opsSet.executeOperations(snapshotSemantics);
				opsSet = executeOperations(opsSet,snapshotSemantics);
				output.write(MessageWritable.write("Performed operations = ",opsSet));
				interaction.trigger();
				output.write(MessageWritable.write("Performed interaction = ",interaction));

				output.write(MessageWritable.write(rootEntity.getJSONDescriptor()));
				cycles++;
				if (interactive) {
					initPause = System.currentTimeMillis();
					System.out.println("Press ENTER to continue");
					try {
						System.in.read();
						startTime += (System.currentTimeMillis() - initPause);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rootEntity.clearCache();
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
	}

}
