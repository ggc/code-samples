package mv.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mv.model.instructions.io.Out;
import mv.view.console.io.GuiWriter;

@SuppressWarnings("serial")
public class OutputPanel extends JPanel implements ActionListener{

	public OutputPanel(){
		setBackground(Color.gray); //Cambia el color del panel
		setBorder(javax.swing.BorderFactory.createTitledBorder("Salida del programa-p"));
		JTextArea ta_salida = new JTextArea();
		ta_salida.setBackground(Color.black);
		ta_salida.setForeground(Color.cyan);
		ta_salida.setCaretColor(Color.green);
		ta_salida.setEditable(false);
		Out.setOutput2(new GuiWriter(ta_salida));
		add(ta_salida);
		setLayout(new GridLayout());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
 		
	}

}
