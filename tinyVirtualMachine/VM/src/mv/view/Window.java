package mv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import mv.controller.Controller;
import mv.model.Instruction;
import mv.model.CPU.Observer;
import mv.model.ProgramMV.Data;

@SuppressWarnings("serial")
public class Window extends JFrame implements Observer{

	private Controller _controller;
	//private ProgramMV _program;
	private JPanel _contentPane;
	private ActionPanel _panelAccion;
	private JPanel _panelMV;
	private JPanel _panelCPU;
	private ProgramPanel _panelPrograma;
	private StackPanel _panelPila;
	private MemoryPanel _panelMemoria;
	private InputPanel _panelEntradas;
	private OutputPanel _panelSalidas;
	private StatusPanel _panelEstado;

	/**
	 * Clase encargada de construir la ventana. No incluye implemetacion de esta ni creacion de oyentes.
	 */
	public Window(Controller controller, String file) {		
		super("Maquina virtual"); 
		_controller = controller;
		//_program = program;
		//Creacion de elementos.
		_panelMV = new JPanel();
		_panelCPU = new JPanel();
		_panelAccion = new ActionPanel(_controller); //PND:Desactivar botones
		_panelPrograma = new ProgramPanel(_controller); //PND: Actualizar en cada Step.
		_panelPila = new StackPanel(_controller);
		_panelMemoria = new MemoryPanel(_controller);
		_panelEntradas = new InputPanel(file);
		_panelEntradas.loadInput(file);
		_panelSalidas = new OutputPanel();
		_panelEstado = new StatusPanel(_controller);

		//Opciones por defecto de la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);

		_contentPane = new JPanel();
		_contentPane.setBackground(Color.black);
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(_contentPane);

		//Definicion del panel de accion.		
		_contentPane.add(_panelAccion, BorderLayout.NORTH);  

		// //Definicion del panel de programa
		_contentPane.add(_panelPrograma, BorderLayout.WEST);  
		
		//Definicion del panel de estado
		_contentPane.add(_panelEstado, BorderLayout.SOUTH);

		//Definicion del panel de la MV. Contendra: Memoria-Pila, Entrada-Salida en una columna
		_panelMV.setLayout(new BoxLayout(_panelMV, BoxLayout.Y_AXIS)); //GridLayout(3, 1, 0, 0)
		_panelMV.add(_panelCPU);       //Add
		_contentPane.add(_panelMV, BorderLayout.CENTER);       //Add

		//Definicion del panel de la CPU, contiene en estado de esta. Pila y Memoria.
		_panelCPU.setLayout(new GridLayout(1, 2, 0, 0));

		//Definicion del panel de la pila.
		_panelCPU.add(_panelPila);       //Add

		//Definicion del panel de memoria. 
		_panelCPU.add(_panelMemoria);       //Add

		//Definicion del panel de entradas. 
		_panelMV.add(_panelEntradas);       //Add
		//Definicion del panel de salidas. 
		_panelMV.add(_panelSalidas);       //Add
		//cpu.setWindow(this);

		
		_controller.addCPUObserver(this);
	
//		this.setExtendedState(MAXIMIZED_BOTH);
	}

	@Override
	public void onStart(Data instructions) {
		_panelPrograma.setProgram(instructions);
		_panelPrograma.refresh();
		
	}

	@Override
	public void onInstructionStart(Instruction instr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInstructionEnd(Instruction instr,
			mv.model.Memory.Data mem,
			mv.model.OperandStack.Data ops,
			mv.model.ControlUnit.Data pc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception error) {
		String message = "";
		message = error.getMessage() + "\n";
		Throwable cause = error;
		while(cause.getCause()!=null){
			cause=cause.getCause();
			message = message + "Causado por: " + cause.getMessage() + "\n";
		} 
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Metodo que ejecuta recursivamente el metodo refresh() de cada subpanel.
	 */
//	public void refresh(){
//		_panelPrograma.refresh();
//		_panelPila.refresh();
//		_panelMemoria.refresh();
//	}
}
