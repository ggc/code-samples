package mv.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mv.model.instructions.io.In;
import mv.view.console.io.GuiReader;

@SuppressWarnings("serial")
public class InputPanel extends JPanel {

	private JTextArea _ta; 

	public InputPanel(String file){
		setBackground(Color.gray); //Cambia el color del panel
		setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada del programa-p"));
		_ta = new JTextArea();
		_ta.setEditable(false);
		_ta.setBackground(Color.black);
		_ta.setForeground(Color.cyan);
		_ta.setCaretColor(Color.green);
		setLayout(new GridLayout());
		add(_ta);
		In.setInput(new GuiReader(_ta));
	}

	public void loadInput(String file){
		Scanner input;
		if(file == null)
			return ;
		try {
			input = new Scanner(new File(file));
			input.useDelimiter("");
			while(input.hasNext())
				_ta.append(input.next());
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
 
}
