package com.ldream.ldream_core;

import java.io.IOException;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.operations.OperationsSet;
import com.ldream.ldream_core.exec.ExecutionStrategy;
import com.ldream.ldream_core.output.ComponentStateWritable;
import com.ldream.ldream_core.output.InteractionWritable;
import com.ldream.ldream_core.output.OperationsWritable;
import com.ldream.ldream_core.output.Output;
import com.ldream.ldream_core.output.Writable;

public class ExecutionEngine implements Runnable {

	private Component rootComponent;
	private ExecutionStrategy executionStrategy;
	private Output output;
	private boolean interactive;
	private int maxCycles;
	private boolean snapshotSemantics;

	public ExecutionEngine(Component rootComponent,ExecutionStrategy executionStrategy,Output output) {
		this.rootComponent = rootComponent;
		this.executionStrategy = executionStrategy;
		this.output = output;
		this.maxCycles = 0;
		this.snapshotSemantics = true;
	}

	public ExecutionEngine(Component rootComponent,ExecutionStrategy executionStrategy,Output output,boolean interactive) {
		this(rootComponent,executionStrategy,output);
		this.interactive = interactive;
	}

	public ExecutionEngine(Component rootComponent,ExecutionStrategy executionStrategy,Output output,int maxCycles) {
		this(rootComponent,executionStrategy,output,false);
		this.maxCycles = maxCycles;
	}

	public ExecutionEngine(Component rootComponent,ExecutionStrategy executionStrategy,Output output,boolean interactive,int maxCycles) {
		this(rootComponent,executionStrategy,output);
		this.maxCycles = maxCycles;
		this.interactive = interactive;
	}

	public void setSnapshotSemantics(boolean snapshotSemantics) {
		this.snapshotSemantics = snapshotSemantics;
	}

	@Override
	public void run() {
		System.out.println("============================");
		System.out.println("DReAM execution engine 0.0.1");
		System.out.println("============================");
		System.out.println(String.format("interactive: %s\nMax cycles: %s\nSnapshot semantics: %s\nExec strategy: %s",
				interactive,
				maxCycles,
				snapshotSemantics,
				executionStrategy.getClass().getSimpleName()));
		
		long startTime = System.currentTimeMillis();
		long initPause = 0;

		int cycles = 0;
		boolean unboundedExecution = false;
		if (maxCycles <= 0)
			unboundedExecution = true;
		Writable w = new ComponentStateWritable(rootComponent);
		output.write(w);
		while (cycles < maxCycles || unboundedExecution) {
			Interaction interaction = executionStrategy.selectInteraction(rootComponent);
			OperationsSet opsSet = rootComponent.getOperationsForInteraction(interaction);

			output.write(new InteractionWritable(interaction));
			output.write(new OperationsWritable(opsSet));

			opsSet.executeOperations(snapshotSemantics);
			interaction.trigger();

			output.write(w);
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
			rootComponent.refresh();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("============================");
		System.out.println(String.format("Completed cycles: %s\nExecution time: %s ms",
				cycles,
				elapsedTime));
	}

}
