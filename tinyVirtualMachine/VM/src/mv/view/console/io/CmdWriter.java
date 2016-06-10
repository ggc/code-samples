package mv.view.console.io;

import java.io.PrintStream;

import mv.ouput.TwoWayWriter;

public class CmdWriter implements TwoWayWriter{
	private PrintStream _output;

	public CmdWriter(PrintStream stream){
		_output = stream;
	}

	@Override
	public void write(char character){
		_output.print(character); 
	}
}
