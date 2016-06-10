package mv.ouput;

import java.io.PrintStream;

public class FileWriter implements TwoWayWriter{

	private PrintStream _output;

	public FileWriter(PrintStream file){
		_output = file;
	}
	
	@Override
	public void write(char character){
		_output.print(character); 
	}
}
