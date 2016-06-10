package mv.view.console.io;

import java.util.Scanner;

import mv.input.Reader;

public class CmdReader implements Reader{
	private Scanner _input;
	private int _character;

	public CmdReader(){
		_input = new Scanner(System.in);
		_character = -1;
		_input.useDelimiter("");
	}

	public int read(){
		if(_input.hasNext())
			_character = _input.next().charAt(0);
		return _character;
	}
}