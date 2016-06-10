/**
 * 
 */
package mv.view;

import org.apache.commons.cli.CommandLine;

/**
 * @author anima
 *
 */
public final class Flags {
//	public static boolean mode;
	public boolean 	h=false, //True si se pidio ayuda. False e.o.c.
							a=false, //True si se especifico prog ASM. False e.o.c.
							i=false, //True si se especifico una salida. False e.o.c.
							o=false,
							m_interactive=false, //True si esta en modo interactivo. False en modo Batch.
							m_window=false, //Sin modo ventana
							m_batch=false,
							batch_in_file=false,
							batch_out_file=false;
							
	
	public void setFlags(CommandLine cmd){
		h=cmd.hasOption('h');
		a=cmd.hasOption('a'); //Siempre debe estar en True.
		if(!cmd.hasOption('m') || cmd.hasOption('m') && cmd.getOptionValue('m').equalsIgnoreCase("batch"))
			m_batch = true;
		else if(cmd.hasOption('m') && cmd.getOptionValue('m').equalsIgnoreCase("interactive"))
			m_interactive = true;
		else if(cmd.hasOption('m') && cmd.getOptionValue('m').equalsIgnoreCase("window"))
			m_window = true; 
		i=cmd.hasOption('i');
		o=cmd.hasOption('o');
		if(!m_interactive && cmd.hasOption('i'))
			batch_in_file=true;
		if(!m_interactive && cmd.hasOption('o'))
			batch_out_file=true;
	}
}
