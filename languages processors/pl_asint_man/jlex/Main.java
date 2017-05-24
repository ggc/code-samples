package jlex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import jlex.ClaseLexica;
import jlex.UnidadLexica;


public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
	     Reader input = new InputStreamReader(new FileInputStream("input.txt"));
	     AnalizadorLexico al = new AnalizadorLexico(input);
	     UnidadLexica unidad;
	     do {
	       unidad = al.yylex();
	       System.out.println(unidad);
	     }
	     while (unidad.clase() != ClaseLexica.EOF);
	    }       
}
