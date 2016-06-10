package mv.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader implements Reader{
	private Scanner _input;
	private int _character;

	public FileReader(String file){
		try {
			_input = new Scanner(new File(file));
			_input.useDelimiter("");
		} catch (FileNotFoundException e) {
			System.err.println("Archivo no encontrado.");
		}
		_character = -1;
	}

	public int read(){
		if(_input.hasNext())
			_character = _input.next().charAt(0);
		else
			_character = -1;
		return _character;
	}

}
