package colegio.gui;

import colegio.Curso;
import colegio.BBDD;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

// Tabla interna que se rellena con los datos de BBDD.listaCursos
public class TablaCursos extends Tabla {

  public TablaCursos (Ventana parent) {

    // Llamamos al constructor de la clase Tabla (la que extiende esta)
    super(parent);

    // Cabeceras de la tabla
    // (la ultima en blanco es para un boton de borrado).
    // DEBEN coincidir con el nombre de las columnas en la BBDD MySQL!  
    String cabecerasColumnas[] = 
      {"ID Curso","Profesores","Alumnos"};

    // El segundo argumento, 0, es para que el numero de filas sea variable
    this.tableModel = new DefaultTableModel(cabecerasColumnas, 0);

    this.setModel(tableModel);    

    // Intentamos rellenar la tabla
    this.rellenar();

  }

  @Override
  protected void incluirMenusAdicionalesEnMenuPopup() {}

  @Override
  protected void setTextosMenuPopup() {

    this.textoAniadir     = "Añadir curso";
    this.textoBorrar      = "Borrar curso seleccionado";

  }

  @Override
  protected void gestionarCambiosRealizados(TableCellListener tcl){

    // La clave es el campo ID, porque:
    // - No puede estar vacio
    // - Debe ser unico en la BBDD
   
    // Por ello, ante cualquier cambio de este campo, 
    // si el nuevo valor del campo ID esta vacio o duplicado, 
    // se muestra un aviso de error y se cancela
    // el cambio

    // Obtenemos el ID del curso (siempre el antiguo,
    // porque es el que tenemos en la BBDD, si no no encontrariamos
    // al curso al buscarlo para hacer los cambios.
    String idEnBBDD = this.getValueAt(tcl.getRow(), 0).toString();

    String oldID = idEnBBDD;
    String newID = idEnBBDD;

    // Si el cambio afecta a la columna donde se almacena el NIF (la 0)...
    if (tcl.getColumn() == 0) {

      oldID = tcl.getOldValue().toString();
      newID = tcl.getNewValue().toString();

      // En este caso, como ha cambiado el nif, acutualizamos el valor
      // de la variable nifEnBBDD para asegurarnos que recoge el antiguo.
      idEnBBDD = oldID;

      // Si el NIF nuevo esta en blanco o ya existe en la BBDD
      if (!BBDD.esIDCursoValido(newID)) {

        ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso
          ("El campo ID del curso no puede estar en blanco ni puede existir ya en la BBDD");

        // Se restaura el valor antiguo
        this.setValueAt(tcl.getOldValue(), tcl.getRow(), tcl.getColumn());
        
        return;

      } 

      // Si llegamos hasta aqui, ha cambiado el campo ID, y el cambio es correcto.
      // Por tanto, actualizamos el valor en la BBDD
      // (esto se hace fuera de este if porque afecta a todos los casos).  

    }

    // Si estamos aqui el cambio que ha habido es correcto,
    // por lo que podemos actualizar la BBDD.

    // Hay que tener en cuenta el caso particular de una insercion de nuevo curso:
    // - Si el ID esta en blanco, el campo modificado es la columna 0 (es decir, el ID),
    // y el campo nuevo es un ID valido, se hace una insercion.
    // - En otro caso, es modificacion.
    if ((idEnBBDD.isEmpty()) &&
        (tcl.getColumn() == 0) &&
        (BBDD.esIDCursoValido(newID))) {

      /*Usuario nuevoUsuario = new Usuario
        (this.getValueAt(this.getSelectedRow(), 0).toString(),
         this.getValueAt(this.getSelectedRow(), 1).toString(),
         this.getValueAt(this.getSelectedRow(), 2).toString(),
         this.getValueAt(this.getSelectedRow(), 3).toString(),
         this.getValueAt(this.getSelectedRow(), 4).toString(),
         this.getValueAt(this.getSelectedRow(), 5).toString(),
         this.getValueAt(this.getSelectedRow(), 6).toString(),
         this.getValueAt(this.getSelectedRow(), 7).toString(),
         this.getValueAt(this.getSelectedRow(), 8).toString(),
         this.getValueAt(this.getSelectedRow(), 9).toString());*/

      Curso nuevoCurso = new Curso("TODO");
      
      BBDD.insertarCurso(nuevoCurso);

    }
    else {

      /*BBDD.actualizarCampoDeUsuario
        (nifEnBBDD,
         this.tableModel.getColumnName(tcl.getColumn()), 
         tcl.getNewValue().toString());*/
      return;
    }

  }

  @Override
  protected void rellenar () {

    ArrayList<Curso> listaCursos = BBDD.getListaCursos();

    if ((listaCursos== null) ||
        (listaCursos.isEmpty()))
      return;

    // Si estamos aqui es porque hay usuarios:
    // los cargamos.
    int i = 0;
    Curso curso = null;
    Object[] fila = {"", "", ""};

    while (i < listaCursos.size()) {
    
      curso = listaCursos.get(i);

      /*fila[0] = usuario.getId();
      fila[1] = usuario.getNombre();
      fila[2] = usuario.getApellido1();
      fila[3] = usuario.getApellido2();
      fila[4] = usuario.getTelefono();
      fila[5] = usuario.getEmail();
      fila[6] = usuario.getDireccion();
      fila[7] = Boolean.toString(usuario.getEsAdministrador());
      fila[8] = Boolean.toString(usuario.getEsProfesor());
      fila[9] = Boolean.toString(usuario.getEsAlumno());*/

      this.tableModel.addRow(fila);

      i++;

    }

  }

  @Override
  protected void addFila() {

    Object[] fila = {"", "", ""};
    tableModel.addRow(fila);

  }

  @Override
  protected void deleteFilaSeleccionada() {

    // Borra la fila seleccionada (si hay alguna)
    int selectedRow = this.getSelectedRow();
    
    if(selectedRow != -1) {

      // Se coge el valor del ID del curso de la fila selccionada.
      // El ID estaba en la posicion 0.
      String id = this.getValueAt(this.getSelectedRow(), 0).toString();

      String fraseId ="";

      if (id.equals(""))
        fraseId = "Se va a borrar el curso seleccionado";
      else
        fraseId = "Se va a borrar el curso con ID " + id;

      boolean avisoAceptado =
        ventanaEnLaQueEstaLaTabla.mostrarVentanaAvisoyComprobarAceptacion
          (fraseId + ".\n" +
           "Esta operacion no se puede deshacer.\n" +
           "¿Desea continuar?");

      if (!avisoAceptado)
        return;

      // Se elimina el usuario de la BBDD y de la tabla
      boolean cursoBorrado = BBDD.borrarCurso(id);

      // Si se pudo eliminar el curso,
      // o si la fila estaba vacia (esto ulimo no se ha probado!), 
      // se elimina la fila
      if (cursoBorrado || id.equals(""))
        tableModel.removeRow(selectedRow);
    }
    else {
      ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso("No hay ningun curso seleccionado");
    }

  }

}
