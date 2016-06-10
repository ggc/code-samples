package mv.view;

import java.awt.Color;
import java.awt.Dimension; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mv.controller.Controller;
import mv.model.OperandStack;
import mv.model.OperandStack.Data;

@SuppressWarnings("serial")
public class StackPanel extends JPanel implements ActionListener, OperandStack.Observer{

	private Controller _controller;
	private JTextArea _taStack;
	private JPanel _panelEditorPila;
	private JButton _btnPush;
	private JButton _btnPop;
	private JTextArea _taValue;
	private JLabel _lblValor;
	private JScrollPane _scrollPane;

	public StackPanel(Controller controller){
		_controller = controller;
		_taStack = new JTextArea("");
		_panelEditorPila = new JPanel();
		_btnPush = new JButton("Push");
		_btnPop = new JButton("Pop");
		_taValue = new JTextArea();
		_lblValor = new JLabel("Valor:");
		_scrollPane = new JScrollPane(_taStack);

		////Definicion del panel de la pila. Contiene la representacion de la pila(text) y el editor de pila.
		setBorder(javax.swing.BorderFactory.createTitledBorder("Pila de operandos"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.gray); //Cambia el color del panel

		_taStack.setEditable(false);
		_taStack.setBackground(Color.black);
		_taStack.setForeground(Color.cyan);
		_taStack.setCaretColor(Color.green);
		_panelEditorPila.setBackground(Color.gray); //Cambia el color del panel
		_panelEditorPila.add(_lblValor);
		_panelEditorPila.add(_taValue);
		_panelEditorPila.add(_btnPush);
		_btnPush.addActionListener(this);
		_panelEditorPila.add(_btnPop);
		_btnPop.addActionListener(this);
		_taValue.setColumns(10);
		_taValue.setBackground(Color.black);
		_taValue.setForeground(Color.cyan);
		_taValue.setCaretColor(Color.green);
		_scrollPane.setMaximumSize(new Dimension(150, 250));
		_scrollPane.setPreferredSize(new Dimension(150,250));		
		add(_scrollPane);       //Add
		add(_panelEditorPila);       //Add
		
		
		_controller.addStackObserver(this);
		
		
		//refresh();
	}

	
	/**
	 * Refresca el JTextArea que muestra el estado de la pila. 
	 */
//	public void refresh(){ 
//		String stack = _cpu.getStack().toString();
//		_taStack.setText("");
//		String splitStack[] = stack.split(" +");
//		for(int i = splitStack.length-1;i >= 0;i--){
//			_taStack.append(splitStack[i] + "\n");
//		}
//
//	}

	
	/* 
	 * Estas son las acciones ejecutadas por los componentes.
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == _btnPush){
			_controller.performPush(Integer.parseInt(_taValue.getText()));
		}
		else if(e.getSource() == _btnPop)
			_controller.performPop();
	}
	
	@Override
	public void onStackChange(Data data) {
		refresh(data);
	}
	
	/**
	 * Crea el pop-up y muestra la informacion de las excepciones lanzadas por el argumento.
	 * @param excepcion de la cual se obtendra el mensaje
	 */
	public void exceptionMessage(Exception e){
		String message = "";
		message = message + e.getMessage() + "\n";
		Throwable cause = e;
		while(cause.getCause()!=null){
			cause=cause.getCause();
			message = message + "Causado por: " + cause.getMessage() + "\n";
		} 
		JOptionPane.showMessageDialog(this, message);
	}


	public void refresh(Data data){
		int[] stackToken = data.getStack();
		_taStack.setText("");
//		String splitStack[] = stack.split(" +");
		for(int i = data.getCntr();i >= 0;i--){
			_taStack.append(stackToken[i] + "\n");
		}	
	}
	
}
