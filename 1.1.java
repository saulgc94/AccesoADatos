package Ejercicio4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Ejercicio4 {
	
	
	
	public static void main (String[]args) {
		String ruta ="/home/sgarciac1/ASIGNATURAS/ACCESS-DD/hola.txt";
		String ruta2="/home/sgarciac1/ASIGNATURAS/ACCESS-DD/hola2.txt";
		ArrayList<String> arrlist = new ArrayList<String>();
	
		File fich = new File(ruta);
		File fich2 = new File(ruta2);
		try {
			System.out.println("Lectura de fichero original: ");
			BufferedReader br = new BufferedReader(new FileReader(fich));
			String linea = br.readLine();
			while(linea!=null) {
				arrlist.add(linea);
				System.out.println(linea);
				linea = br.readLine();
			}
			br.close();
			Collections.reverse(arrlist);

			System.out.println("");

			BufferedWriter bw = new BufferedWriter(new FileWriter(fich2));
			for(int i=0;i<arrlist.size();i++) {
				bw.write(arrlist.get(i));
				bw.newLine();
				
			}
			//for(int i=0;i<arrlist.size();i++) {
			 //System.out.println((arrlist.get(i).toString()));
			//}
			bw.close();


		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	     try {
			//BufferedWriter bfw=new BufferedWriter(new FileWriter(fich2));
		    BufferedWriter bfw=new BufferedWriter(new FileWriter(fich2, true));
		    bfw.newLine();
		    bfw.write("Saul García Climent");
		    bfw.newLine();
		    bfw.close();
		   
			System.out.println("Lectura de fichero copiado: ");
			BufferedReader bfr= new BufferedReader(new FileReader(fich2));
			String linea = bfr.readLine();
			while(linea!=null) {
				System.out.println(linea);
				linea = bfr.readLine();
			}
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     
	 
	

	}
}
