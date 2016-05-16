/**
 * 
 */
package mv.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Anima
 *
 */
public class ProgramParser {



	//Atributos

	//Constructor

	//Metodos

	public String parseProgram (String fileName) throws IOException{

		String strtok = ""; //String auxiliar
		String line = null; //Linea leida del fichero.
		StringBuffer strBuff = new StringBuffer(""); //StringBuffer al que concatenaremos cada strtok
		int j = 0;
		boolean keepReading = false; //Booleano que indica si tiene que seguir leyendo la linea.
		BufferedReader br = null;
 			FileInputStream fileInStream = new FileInputStream(new File(fileName));
			br = new BufferedReader(new InputStreamReader(fileInStream));
			while ((line=br.readLine()) != null){//recorre lineas
				strtok = "";
				keepReading = true;
				j = 0;
				while(j < line.length()){//Recorre char a char
					if(line.charAt(j)== ';'){
						break;
					}
					keepReading = true;
					strtok += line.charAt(j);
					j++;
				}
				if(keepReading){
					strBuff.append(strtok + "\n"); //Concatena strtok y un salto de carro.
				}
			}

			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Imposible cerrar BufferedReader");
			}		
		return strBuff.toString().trim(); //El trim es por un espacio que se cuela al principio.
	}
}
