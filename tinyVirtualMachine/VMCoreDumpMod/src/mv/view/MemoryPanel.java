package mv.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import mv.controller.Controller;
import mv.model.Memory.Data;
import mv.model.Memory.Observer;

@SuppressWarnings("serial")
public class MemoryPanel extends JPanel implements ActionListener, Observer{
	private Controller _controller;
	private JPanel _panelEditorMemoria;
	private JPanel _areaMemoria;
	private JTable _memoryTable;
	private TableModel _tableModel;
	private JButton _btnWrite;
	private JLabel _lblAddress; 
	private JLabel _lblValue;
	private JTextArea _taAddress;
	private JTextArea _taValue;
	private Data _data;

	//private int _address;
	//private int _value;

	public MemoryPanel(Controller controller){
		_controller = controller;
		_areaMemoria = new JPanel();
		_panelEditorMemoria = new JPanel();
		_btnWrite = new JButton("Write");
		_lblAddress = new JLabel("Pos:");
		_lblValue = new JLabel("Val:");
		_taAddress = new JTextArea();
		_taValue = new JTextArea();
		_tableModel = new TableModel();
		_tableModel.getValueAt(1, 1);
		//
		//		/****************/
		//		/*Mal. Ejemplo.*/
		//		/**************/
		//		Object[][] data = {	{"100", "0"},
		//				{"104", "7"},
		//				{"108", "4"},
		//				{"112", "97"},
		//				{"116", "-1"}};
		//		String[] columnNames = {"Valor", "Direccion"};//array de String's con los t�tulos de las columnas
		setBackground(Color.gray); //Cambia el color del panel
		_memoryTable = new JTable(_tableModel);//se crea la Tabla
		_memoryTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		_memoryTable.setBackground(Color.black);
		_memoryTable.setForeground(Color.cyan);
		_memoryTable.setGridColor(Color.cyan);
		//Fin ejemplo//


		//Definicion del panel que representa la memoria.
		setBorder(javax.swing.BorderFactory.createTitledBorder("Memoria de la m�quina"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_areaMemoria.add(new JScrollPane(_memoryTable));	
		_areaMemoria.setLayout(new GridLayout(1, 1, 0, 0));
		add(_areaMemoria);       //Add
		//Definicion del panel editor de la memoria. Permite hacer un write en memoria. 
		_panelEditorMemoria.setBackground(Color.gray); //Cambia el color del panel
		_panelEditorMemoria.add(_lblAddress);
		_panelEditorMemoria.add(_taAddress);
		_taAddress.setColumns(10);
		_taAddress.setBackground(Color.black);
		_taAddress.setForeground(Color.cyan);
		_taAddress.setCaretColor(Color.green);
		_panelEditorMemoria.add(_lblValue);
		_panelEditorMemoria.add(_taValue);
		_taValue.setColumns(10);
		_taValue.setBackground(Color.black);
		_taValue.setForeground(Color.cyan);
		_taValue.setCaretColor(Color.green);
		_panelEditorMemoria.add(_btnWrite);       //Add
		_btnWrite.addActionListener(this);
		add(_panelEditorMemoria);        //Add
		
		
		_controller.addMemoryObserver(this);

	}



	@Override
	public void actionPerformed(ActionEvent e) {
		int address = Integer.parseInt(_taAddress.getText());
		int value = Integer.parseInt(_taValue.getText());
		_controller.performWrite(address, value);
	}


	@Override
	public void onMemoryChange(Data data) {
		_data = data;
		refresh();
	}

	public void refresh(){
		_tableModel.fireTableDataChanged();
	}


	//Creacion del modelo de la tabla. Sera anadido a la JTable
	public class TableModel extends AbstractTableModel{

		private String[] _columnName= {"Address","Value"};
		public String getColumnName(int i){
			return _columnName[i];
		}
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			if(_data != null)
				return _data.getCntr() + 1;
			else
				return 0;
		}

		//Este metodo aunque es un getter, sirve para definir cada celda. A efectos es un setter
		@Override
		public Object getValueAt(int row, int col) { //En col0 las direcciones y en col1 los valores
			String cell = null;
			if(_data == null)
				return "";

			if(col==0){
				if(_data.getData()[row] != null)
					cell = "" + _data.getData()[row].getPos();
			}

			else{
				if(_data.getData()[row] != null)
					cell = "" + _data.getData()[row].getValue();
			}

			return cell;
		}


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
