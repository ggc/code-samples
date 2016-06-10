package mv.view.console.io;

import javax.swing.JTextArea;

import mv.ouput.TwoWayWriter;

public class GuiWriter implements TwoWayWriter{

	private JTextArea _output;

	public GuiWriter(JTextArea textArea){
		_output = textArea;
	}
	
	@Override
	public void write(char character){
		_output.append("" + character); 
	}

}
