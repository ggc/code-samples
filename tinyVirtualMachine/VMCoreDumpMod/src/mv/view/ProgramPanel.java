package mv.view;

import java.awt.Color; 
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mv.controller.Controller;
import mv.model.ControlUnit;
import mv.model.ProgramMV.Data;

@SuppressWarnings("serial")
public class ProgramPanel extends JPanel implements ControlUnit.Observer{

	private Controller _controller;
	private JTextArea _textArea;
	private Data _program;
	private int _cp = 0;
	//	private ProgramMV _program;

	public ProgramPanel(Controller controller){
		_controller = controller;
		_cp = 0;
		setBackground(Color.gray); //Cambia el color del panel
		this.setMinimumSize(new Dimension(1000, 1000));
		_textArea = new JTextArea("",20	,12);
		_textArea.setTabSize(15);
		_textArea.setBackground(Color.black);
		_textArea.setForeground(Color.cyan);
		_textArea.setCaretColor(Color.green);
		//this.setBackground(new Color(150, 150, 150)); //Cambia el color del panel
		this.setBorder(javax.swing.BorderFactory.createTitledBorder("Programa"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JScrollPane(_textArea));
		//refresh();

		_controller.addControlUnitObserver(this);

	}
	@Override
	public void onCPchange(ControlUnit.Data cpData) {
		_cp = cpData.getCp();
		refresh();
	}

	@Override
	public void onHalt() {
		// TODO Auto-generated method stub
	}

	public void setProgram(Data program){
		_program = program;
	}
	/**
	 * Refresca el JTextArea que muestra el programa. 
	 */
	public void refresh(){
		int i = 0;
		_textArea.setText("");
		while(_program.getInstructions()[i] != null){
			_textArea.append(i + " : " + _program.getInstructions()[i].toString());
			if(i == _cp)
				_textArea.append("<---\n");
			else
				_textArea.append("\n");
			i++;
		}
	}


}
