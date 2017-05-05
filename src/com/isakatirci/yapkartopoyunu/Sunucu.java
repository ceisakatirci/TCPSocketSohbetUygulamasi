/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isakatirci.yapkartopoyunu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author isa
 */
public class Sunucu extends javax.swing.JFrame {

    private ArrayList<Karsilayan> karsilayanlar = new ArrayList<Karsilayan>();
    private ServerSocket server;
    private Socket connection;
    private int counter = 0;

    private class Karsilayan extends Thread {

        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;

        @Override
        public void run() {
            try {
                try {
                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());
                    displayMessage("\nGot I/O streams\n");
                    String message = "Connection successful";
                    sendData(message);
                    setTextFieldEditable(true);
                    do {
                        try {
                            message = (String) input.readObject();
                            displayMessage("\n" + message);
                        } catch (ClassNotFoundException classNotFoundException) {
                            displayMessage("\nUnknown object type received");
                        }

                    } while (!message.equals("CLIENT>>> TERMINATE"));

                } catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } finally {
                    displayMessage("\nTerminating connection\n");
                    setTextFieldEditable(false);

                    try {
                        output.close();
                        input.close();
                        connection.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        public Karsilayan(Socket connection) {
            this.connection = connection;
        }

        private void sendData(String message) {
            try {
                output.writeObject("SERVER>>> " + message);
                output.flush();
                displayMessage("\nSERVER>>> " + message);
            } catch (IOException ioException) {
                displayArea.append("\nError writing object");
            }
        }
    }

    public void runServer() {
        try {
            server = new ServerSocket(12345, 100);
            while (true) {
                displayMessage("\nWaiting for connection\n");
                connection = server.accept();
                ++counter;
                displayMessage("Connection " + counter + " received from: "
                        + connection.getInetAddress().getHostName());
                Karsilayan karsilayan = new Karsilayan(connection);
                karsilayanlar.add(karsilayan);
                karsilayan.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Creates new form MerkeziSunucu
     */
    public Sunucu() {
        super("Sunucu");
        initComponents();
        enterField.setEditable(false);
        enterField.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                for (Karsilayan karsilayan : karsilayanlar) {
                    karsilayan.sendData(event.getActionCommand());
                    enterField.setText("");
                }
            }
        }
        );
    }

    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                displayArea.append(messageToDisplay);
            }
        }
        );
    }

    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                enterField.setEditable(editable);
            }
        }
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        enterField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        displayArea.setColumns(20);
        displayArea.setRows(5);
        jScrollPane1.setViewportView(displayArea);

        enterField.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(enterField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(enterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */

 /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http:
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sunucu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sunucu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sunucu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sunucu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Sunucu merkeziSunucu = new Sunucu();
                merkeziSunucu.setVisible(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        merkeziSunucu.runServer();
                    }
                }).start();

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea displayArea;
    private javax.swing.JTextField enterField;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
