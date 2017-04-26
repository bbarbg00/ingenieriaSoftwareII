package colegio.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
 
// Codigo sacado y adaptado de:
// http://www.zentut.com/java-swing/simple-login-dialog/
public class ChangePasswordDialog extends JDialog {

    private JPasswordField campoPassword;
    private JLabel etiquetaPassword;
    private JButton botonAceptar;
    private JButton botonCancelar;
 
    private boolean cambioAceptado = false;    

    public ChangePasswordDialog(final Frame parent) {

        super(parent, "Cambio de contraseña", true);

        // Si se cierra esta ventana, se cierra la aplicacion
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent arg0) {
             dispose();
             parent.dispose();
          }

        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        etiquetaPassword = new JLabel("Contraseña nueva: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(etiquetaPassword, cs);
 
        campoPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(campoPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        botonAceptar = new JButton("Aceptar");
 
        botonAceptar.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                cambioAceptado = true; 
                dispose();            
            }

        });

        // boton cancelar (cierra la ventan de login y la aplicacion
        botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(botonAceptar);
        bp.add(botonCancelar);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        this.setVisible(true);

    }

    public String getPassword() {
        return new String(campoPassword.getPassword());
    }

    public boolean getCambioAceptado() {

       return this.cambioAceptado;
    }
 
}
