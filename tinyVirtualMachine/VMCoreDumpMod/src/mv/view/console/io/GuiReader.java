package mv.view.console.io;
 
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import mv.input.Reader;

public class GuiReader implements Reader{

	private JTextArea _ta;
	private int _character;
	private int _pointer;

	public GuiReader(JTextArea ta){
		_ta = ta;
		_character = -1;
		_pointer = 0;
	}

	public int read(){
		String aux = "";
		try {
			aux = _ta.getText(_pointer, 1);
		} catch (BadLocationException e) { e.printStackTrace(); } 
		if(_pointer < _ta.getText().length()){
			_character = aux.charAt(0);
			_ta.replaceRange("*", _pointer, _pointer+1);
			_pointer++;
		}
		else
			_character = -1;
		return _character;
	}
}
