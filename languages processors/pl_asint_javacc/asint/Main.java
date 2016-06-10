package asint;
import java.io.FileReader;
public class Main{
   public static void main(String[] args) throws Exception {
      AnalizadorSintactico asint = new AnalizadorSintactico(new FileReader(args[0]));
	  asint.Sp();
   }
}