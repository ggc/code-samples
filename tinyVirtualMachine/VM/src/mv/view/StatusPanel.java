package mv.view;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mv.controller.Controller;
import mv.model.OperandStack.Data;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel implements  mv.model.ControlUnit.Observer, 
mv.model.Memory.Observer, 
mv.model.OperandStack.Observer{

	private Controller _controller;
	private JLabel _info;
	private JLabel _numInstructions;
	private JCheckBox _firstCheck;
	private JLabel _firstMessage;
	private JCheckBox _secondCheck;
	private JLabel _secondMessage;
	private int _instructionCounter;
	private boolean _stackChanged;
	private boolean _memoryChanged;


	public StatusPanel(Controller controller){
		_controller = controller;
		_instructionCounter = 0;
		_info = new JLabel("Numero de intrucciones ejecutadas: ");
		_numInstructions = new JLabel("0");
		_firstCheck = new JCheckBox();
		_firstCheck.setBackground(Color.gray);
		_firstMessage = new JLabel("Pila modificada: ");
		_secondCheck = new JCheckBox();
		_secondCheck.setBackground(Color.gray);
		_secondMessage = new JLabel("Memoria modificada: ");
		setBackground(Color.gray);

		add(_info);
		add(_numInstructions);
		add(_firstCheck);
		add(_firstMessage);
		add(_secondCheck);
		add(_secondMessage);

		_controller.addControlUnitObserver(this);
		_controller.addStackObserver(this);
		_controller.addMemoryObserver(this);

	}

	@Override
	public void onCPchange(mv.model.ControlUnit.Data cpData) {
		_instructionCounter++;
		_numInstructions.setText("" + _instructionCounter);
		if(_stackChanged && !_firstCheck.isSelected())
			_firstCheck.doClick();
		else if(!_stackChanged && _firstCheck.isSelected())
			_firstCheck.doClick();
		if(_memoryChanged && !_secondCheck.isSelected())
			_secondCheck.doClick();
		else if(!_memoryChanged && _secondCheck.isSelected())
			_secondCheck.doClick();
		_memoryChanged=false;
		_stackChanged=false;
	}
	@Override
	public void onStackChange(Data data) {
		_stackChanged = true;

	}

	@Override
	public void onMemoryChange(mv.model.Memory.Data data) {
		_memoryChanged = true;
	}

	@Override
	public void onHalt() {
		// TODO Auto-generated method stub

	}



}
