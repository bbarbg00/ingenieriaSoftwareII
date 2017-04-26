package colegio.gui;

import colegio.BBDD;

import javax.swing.SwingUtilities;

public class Main{

  public static void main(String[] args){

    try{

      // Conectamos a la base de datos y 
      // obtenemos los datos que contiene
      BBDD bd = new BBDD();
    
    } catch (Exception excepcion){}

    // Creamos la interfaz grafica
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Ventana ventana = new Ventana();
      }
    });

  }
}
