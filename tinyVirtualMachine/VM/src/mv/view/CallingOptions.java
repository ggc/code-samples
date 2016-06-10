package mv.view;

import org.apache.commons.cli.*;

public class CallingOptions {

	//Atributos

	//Constructor

	//Metodos
	/**
	 * Metodo que construye el conjunto de optiones posibles: h,a,m,i y o
	 * @return
	 */
	public Options buildOptions(){
		Options o = new Options();
		//@SuppressWarnings("static-access")
		
		Option option_a = new Option("a", "asm", true, "Fichero con el codigo en ASM del programa a ejecutar. Obligatorio en modo batch.");
		Option option_h = new Option("h", "help", false, "Muestra esta ayuda");
		Option option_i = new Option("i", "in", true, "Fichero del programa de la maquina-p");
		Option option_m = new Option("m", "mode",true, "Modo de funcionamiento (batch | interactive | window). Por defecto, batch.");
		Option option_o = new Option("o", "out", true, "Fichero donde se guarda la salida del programa de la maquina-p");
 		
		o.addOption(option_a);
		o.addOption(option_o);
		o.addOption(option_m);
		o.addOption(option_i);
		o.addOption(option_h);

		return o;
	}

	/**
	 * Metodo que muestra la ayuda.
	 * @param options
	 */
	public void showHelp(Options options) {
		String syntax = "tp.pr3.mv.Main [-a <asmfile>] [-h] [-i <infile>] [-m <mode>] [-o <outfile>]";
		HelpFormatter h = new HelpFormatter();
		h.printHelp(72,syntax,"",options, "");
	}

}
