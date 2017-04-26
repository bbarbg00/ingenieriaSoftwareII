package colegio.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
 
// Codigo sacado y adaptado de:
// http://www.zentut.com/java-swing/simple-login-dialog/
public class WarningDialog extends JDialog {
 
    private JLabel etiquetaMensaje;
    private JButton botonAceptar;
    private JButton botonCancelar;

    private boolean accionAceptada = false;
 
    public WarningDialog(Frame parent, String mensaje, boolean mostrarBotonCancelar) {

        super(parent, "Aviso!", true);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent arg0) {
             dispose();
          }

        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        etiquetaMensaje = new JLabel(mensaje);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(etiquetaMensaje, cs);
         
        botonAceptar = new JButton("Aceptar");
 
        botonAceptar.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {

                accionAceptada = true;
                dispose();
            }
        });

        JPanel bp = new JPanel();
        bp.add(botonAceptar);

        // boton cancelar (cierra la ventana)
        if (mostrarBotonCancelar) {

          botonCancelar = new JButton("Cancelar");
          botonCancelar.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
          });

          bp.add(botonCancelar);
        }

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        this.setVisible(true);

    }
 
    public boolean accionAceptada() {
        return accionAceptada;
    }
}
