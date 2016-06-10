package mv.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mv.controller.Controller;
import mv.model.ControlUnit.Data;
import mv.model.ControlUnit.Observer;

@SuppressWarnings("serial")
public class ActionPanel extends JPanel implements Observer{

	private Controller _controller;
	private JButton _btnStep;
	private JButton _btnRun;
	private JButton _btnPause;
	private JButton _btnStop; 
	private JButton _btnUndo;
	private boolean _stopRun;

	public ActionPanel(final Controller controller){
		_controller = controller;
		_stopRun = false;
		setBackground(Color.gray); //Cambia el color del panel
		_btnStep = new JButton(new ImageIcon("step.png"));
		_btnRun = new JButton(new ImageIcon("run.png"));
		_btnPause = new JButton(new ImageIcon("pause.png"));
		_btnStop = new JButton(new ImageIcon("exit.png"));
		_btnUndo = new JButton("Step backward");
		_btnStep.setBackground(Color.gray);
		_btnRun.setBackground(Color.gray);
		_btnPause.setBackground(Color.gray);
		_btnStop.setBackground(Color.gray);
		_btnUndo.setBackground(Color.gray);
		setBorder(BorderFactory.createTitledBorder("Acciones"));
		add(_btnStep);
		add(_btnRun);
		add(_btnPause);
		add(_btnStop);
		add(_btnUndo);

		
		_controller.addControlUnitObserver(this);
		
		/*
		 * Acciones que realizaran la pulsacion de los botones. 
		 */
		_btnStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_controller.performStep();
			}
		});
		
		_btnRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while(!_stopRun)
				_controller.performStep();//Completar el bucle
			}
		});
		
		_btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Ya veremos que pasa.
			}
		});
		
		_btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(_btnStop, "Do you want to stop execution and leave?", "Exit", JOptionPane.OK_CANCEL_OPTION) == 0)
					System.exit(0);
			}
		});

		_btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_controller.performUndo();
			}
		});
	}

	@Override
	public void onCPchange(Data cpData) {
	}

	@Override
	public void onHalt() {
		_stopRun = true;
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

}
