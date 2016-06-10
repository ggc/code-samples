package mv;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import mv.controller.BatchController;
import mv.controller.Controller;
import mv.controller.InteractiveController;
import mv.controller.SwingController;
import mv.input.FileReader;
import mv.input.NullReader;
import mv.input.Reader;
import mv.model.CPU;
import mv.model.ProgramLoader;
import mv.model.ProgramParser;
import mv.model.instructions.io.In;
import mv.model.instructions.io.Out;
import mv.ouput.FileWriter;
import mv.ouput.NullWriter;
import mv.ouput.TwoWayWriter;
import mv.view.CallingOptions;
import mv.view.Flags;
import mv.view.Window;
import mv.view.console.io.CmdReader;
import mv.view.console.io.CmdWriter;

public class Main {

	public static void main(String[] args){
		/////////////////////////////////////////////////////////////////////////////////
		//Transformacion de los argumentos en opciones mediante la libreria Commons.cli
		/////////////////////////////////////////////////////////////////////////////////
		CommandLineParser cmdParser = new PosixParser(); //Parser de la libreria commons.cli
		CallingOptions cOptions = new CallingOptions();
		Options options = cOptions.buildOptions(); //Inicializador de optiones
		Flags flags = new Flags();
		Scanner sc = null;
		String iFile = null;
		try {
			CommandLine cmd;

			cmd = cmdParser.parse(options,args);
			//Un CommandLine es la tira de argumentos parseada
			flags.setFlags(cmd);
			if(cmd.getOptionValue('i') != null)
				iFile= cmd.getOptionValue('i');
			if(!flags.m_batch && !flags.m_interactive && !flags.m_window){
				System.err.println("Uso incorrecto: Modo incorrecto (parametro -m|--mode)"
						+ "\nUse -h|--help para más detalles.");
				System.exit(2);
			}

			///////////////////////////////////////////////////////////////////////////////
			//Obligatoriedad de los parametros/////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////
			if(!flags.m_interactive && !cmd.hasOption('a')){
				System.err.println("Uso incorrecto: Fichero ASM no especificado.\n"
						+ "Use -h| --help para mas detalles.");
				System.exit(1); //Salida del main
			}

			if(flags.h){ //Si en el CommandLine esta la opcion -h...
				cOptions.showHelp(options); //Metodo que muestra la ayuda.
				System.exit(0); //Salida del main
			}

			///////////////////////////////////////////////////////////////////////////////
			//Establece la entrada del programa segun los parametros///////////////////////
			///////////////////////////////////////////////////////////////////////////////
			Reader input1;
			if(flags.i){
				input1 = new FileReader(iFile);
			}
			else
				if(flags.m_interactive)
					input1 = new NullReader();
				else
					input1 = new CmdReader();
			In.setInput(input1);

			///////////////////////////////////////////////////////////////////////////////
			//Establece la salida del programa en funcion de los parametros////////////////
			///////////////////////////////////////////////////////////////////////////////
			TwoWayWriter output1;
			if(flags.o)
				output1 = new FileWriter(new PrintStream(cmd.getOptionValue('o')));
			else
				if(flags.m_interactive)
					//Se abre un canal de impresion nuevo del cual se implementa el metodo write. En el cual no se implemeta codigo.
					output1 = new NullWriter();
				else
					output1 = new CmdWriter(new PrintStream(System.out));
			Out.setOutput1(output1);


			///////////////////////////////////////////////////////////////////////////////
			//Lectura del programa desde fichero o consola
			///////////////////////////////////////////////////////////////////////////////
			//			try{
			ProgramParser fr = new ProgramParser();//Parser del programa. Quiza debamos cambiar el nombre a ProgramParser o algo asi.

			if(!flags.m_interactive || (flags.m_interactive && cmd.hasOption('a')))
				sc = new Scanner(fr.parseProgram(cmd.getOptionValue('a'))); //Se inicializa el escaner con un archivo.
			else{
				sc = new Scanner(System.in);
				System.out.print("Introduce el programa fuente\n>");
			}
			//System.exit(2);//Que no se olvide.
			
			ProgramLoader.setProgram(flags, sc);

			///////////////////////////////////////////////////////////////////////////////
			//Ejecucion del programa
			///////////////////////////////////////////////////////////////////////////////
			CPU cpu = new CPU();
			cpu.loadProgram(ProgramLoader.returnProgram());

			///////////////////////////////////////////////////////////////////////////////
			//En modo no ventana///////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////

			if(flags.m_batch){
				Controller cBatch = new BatchController(cpu);
				cBatch.start();
			}else if(flags.m_interactive){
				sc = new Scanner(System.in); //Se inicializa el escaner con la estrada de consola.
				Controller cInteractive = new InteractiveController(cpu, sc);//Revisar este scanner!!!
//				Console console = new Console(cInteractive);
				cInteractive.start();
			}else{
				sc = new Scanner(System.in); //Se inicializa el escaner con la estrada de consola.
				Controller cSwing = new SwingController(cpu);
				Window gui = new Window(cSwing, iFile);
				gui.setVisible(true); 
				cSwing.start();
			}
		} catch (ParseException e1) {
		} catch (IOException e){

		}

	}

}