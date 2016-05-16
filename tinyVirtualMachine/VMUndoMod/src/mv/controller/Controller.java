package mv.controller;

import mv.controller.cmdprompt.commands.Step;
import mv.model.CPU;
import mv.model.ControlUnit;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.commands.CommandException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.memory.Write;
import mv.model.instructions.stack.Pop;
import mv.model.instructions.stack.Push;



public abstract class Controller {


	protected CPU _cpu;

	public Controller(CPU model) {
		_cpu = model;
	}

	public void performPush(int val) {
		try {
			_cpu.cpuExecute(new Push(val));
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void performPop() {
		try {
			_cpu.cpuExecute(new Pop());
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void performWrite(int pos, int val) {
		try {
			_cpu.cpuExecute(new Write(pos, val));
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void performStep() {
		Step step = new Step();
		try {
			step.executeCommand();
		} catch (InstructionException e) {
			e.printStackTrace();
		}
	}
	
	public void performUndo(){
		_cpu.undo();
	}
	
	public void performRedo(){
		//_cpu.redo();
	}

	public void addStackObserver(OperandStack.Observer obs) {
		_cpu.addObserver(obs);
	}

	public void addMemoryObserver(Memory.Observer obs) {
		_cpu.addObserver(obs);
	}

	public void addCPUObserver(CPU.Observer obs) {
		_cpu.addObserver(obs);
	}

	public void addControlUnitObserver(ControlUnit.Observer obs) {
		_cpu.addObserver(obs);
	}

	public void start(){
		_cpu.start();
	}




}
