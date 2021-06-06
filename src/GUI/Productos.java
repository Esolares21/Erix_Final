/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import ConexionBD.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class Productos extends javax.swing.JDialog {

    //METODO LIMPIAR, ENVIARA TEXTOS VACIOS A MIS CAMPOS //
    public void Limpiar(){
        IDP.setText("");
        NP.setText("");
        DP.setText("");
        FA.setDateFormatString("");
        FV.setDateFormatString("");
        PP.setText("");
        
    }
    
    public Productos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Conexion basedatos = new Conexion();
        Connection conn;
        conn= basedatos.ObtenerConexion();
    }
     //metodo guardar que enviara mis datos de los campos hacia la Base de Datos//
    public void guardar(){
       int cod;
       String idpr,nombrep,descripcionp,fechaad, fechave,precio;
       idpr= IDP.getText();
       nombrep= NP.getText();
       descripcionp= DP.getText();
       fechaad=new SimpleDateFormat("yyyy-MM-dd").format(FA.getDate());
       fechave=new SimpleDateFormat("yyyy-MM-dd").format(FV.getDate());
       precio=PP.getText();
       
       if(idpr.equals("")|| nombrep.equals("")||descripcionp.equals("")||fechaad.equals("")|| fechave.equals("")||
               precio.equals("")){
           System.out.println("Porfavor ingrese todos los "
                   + "datos de los camos");
       }
       else {
           try{
               Conexion basedatos= new Conexion();
               Connection conn;
               conn= basedatos.ObtenerConexion();
               PreparedStatement ps= null;
               ResultSet consulta7 = null;
               
               String sql="insert productos (idproducto, nombre, descripcion, fechaa, fechav, precio, estado) values (?,?,?,?,?,?,1)";
               
               ps=conn.prepareStatement(sql);
               String id= IDP.getText();
               ps.setString(1, id);
               String nomb= NP.getText();
               ps.setString(2, nomb);
               String des= DP.getText();
               ps.setString(3, des);
               String fecha1 = new SimpleDateFormat("yyyy-MM-dd").format(FA.getDate());
               ps.setString(4, fecha1);
               String fecha2 = new SimpleDateFormat("yyyy-MM-dd").format(FV.getDate());
               ps.setString(5, fecha2);
               String pre= PP.getText();
               ps.setString(6, pre);
               
               ps.execute();

               JOptionPane.showMessageDialog(this, "Los datos han sido almacenados de forma correcta");
               Limpiar();
               mostrar();
           }catch(Exception e){
               JOptionPane.showMessageDialog(this, "Error al almacenar la informacion");
           }
       }
   }
   // metodo de mostrar que llamara los datos desde la base de datos y los mostrara en mi tabla tanto columnas como filas//
    public void mostrar(){
       Conexion basedatos= new Conexion();
               Connection conn;
               conn= basedatos.ObtenerConexion();
               PreparedStatement ps= null;
               ResultSet consulta = null;

               try{
                   Statement comando = conn.createStatement();
                   consulta= comando.executeQuery("select idproducto, nombre, descripcion, fechaa, fechav, precio from productos where estado !=0;");
                   DefaultTableModel modelo= new DefaultTableModel();
                   this.tabla.setModel(modelo);
                   ResultSetMetaData rmd = consulta.getMetaData();

                   int numcol = rmd.getColumnCount();

                   for(int i=0; i<numcol;i++){
                       modelo.addColumn(rmd.getColumnLabel(i+1));
                       }
                  
                        while(consulta.next()){
                       Object [] fila= new Object[numcol];
                       
                               
                       for(int i=0; i<numcol;i++){
                       fila[i]= consulta.getObject(i+1);
                       
                       for(int a=0; a<numcol;a++){
                            
                            fila[a]= consulta.getObject(a+1);
                       }
                       }
                       modelo.addRow(fila);
                       
                       }      

                   consulta.first();
                   
               }catch(Exception e){
                   System.out.println("Error"+e);
               }
               
   }

    //se crea el metodo eliminar que enviara codigo de estado 0 al registro correspondiente que se haya seleccionado//
    public void Eliminar (){
    int filInicio= tabla.getSelectedRow();
    int numfila= tabla.getSelectedRowCount();
    
    ArrayList<String>listapersona= new ArrayList<>();
    String cod= null;
    
    if(filInicio>=0){
        for(int i=0; i<numfila; i++){
            cod= String.valueOf(tabla.getValueAt(i+filInicio,0));
            listapersona.add(i,cod);
        }
        for (int j=0; j<numfila;j++){
            int resp = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro:  "+listapersona.get(j)+"? ");
            if(resp==0){
                int filAfectada= 0;
                
                try{
                     Conexion basedatos= new Conexion();
                     Connection conn;
                     conn= basedatos.ObtenerConexion();
                     PreparedStatement ps= null;
                     ResultSet consulta = null;
                     ps= conn.prepareStatement("UPDATE productos SET estado= 0 WHERE idproducto="+cod);
                     
                     int res= ps.executeUpdate();

                     if(res>0){
                         JOptionPane.showMessageDialog(null, "Los datos han sido Eliminados");
                         mostrar();
                     }else{
                         JOptionPane.showMessageDialog(null, "Error al Eliminar los datos");
                     }
                     
                }catch(Exception e){
                    System.err.println(e);
                }
            }
        }
    }else{
        JOptionPane.showMessageDialog(null, "Porfavor elija un registro para eliminar");
    }
}
    
    //metodo actualizar que se encargara de modificar los datos de un registro ya guardado//
    public void actualizar (){
    int filInicio= tabla.getSelectedRow();
    int numfila= tabla.getSelectedRowCount();
    
    ArrayList<String>listapersona= new ArrayList<>();
    String cod= null;
    
    if(filInicio>=0){
        for(int i=0; i<numfila; i++){
            cod= String.valueOf(tabla.getValueAt(i+filInicio,0));
            listapersona.add(i,cod);
        }
        for (int j=0; j<numfila;j++){
            int resp = JOptionPane.showConfirmDialog(null, "Esta seguro que desea actualizar el registro:  "+listapersona.get(j)+"? ");
            if(resp==0){
                int filAfectada= 0;
                 try{
                     desbloquear();
                     Conexion basedatos= new Conexion();
                     Connection conn;
                     conn= basedatos.ObtenerConexion();
                    PreparedStatement ps= null;
                     ResultSet consulta = null;
                     
                     ps= conn.prepareStatement("UPDATE productos SET idproducto=?, nombre=?, descripcion=?, fechaa=?, fechav=?, precio=? WHERE idproducto="+cod);
                     ps.setString(1, IDP.getText());
                     ps.setString(2, NP.getText());
                     ps.setString(3, DP.getText());
                     String fecha = new SimpleDateFormat("yyyy-MM-dd").format(FA.getDate());
                     ps.setString(4, fecha);
                     String fecha2 = new SimpleDateFormat("yyyy-MM-dd").format(FV.getDate());
                     ps.setString(5, fecha2);
                     ps.setString(6, PP.getText());
                    
                     int res= ps.executeUpdate();
                     if(res>0){
                         JOptionPane.showMessageDialog(null, "Los datos han sido Actualizados");
                         mostrar();
                         Limpiar();
                     }else{
                         JOptionPane.showMessageDialog(null, "Error al Actualizar los datos");
                     }
                     conn.close();
                }catch(Exception e){
                    System.err.println(e);
                }
                
            }
        }
    }else{
        JOptionPane.showMessageDialog(null, "Porfavor elija un registro para Actualizar");
    }
}
    
//metodo cargar enviara los datos de mi seleccion en la tabla hacia mis campos para poder modificarlos//
    public void cargar(){
    if(tabla.getSelectedRowCount()>0){
        IDP.setText(tabla.getValueAt(tabla.getSelectedRow(), 0).toString());
        NP.setText(tabla.getValueAt(tabla.getSelectedRow(), 1).toString());
        DP.setText(tabla.getValueAt(tabla.getSelectedRow(), 2).toString());
        PP.setText(tabla.getValueAt(tabla.getSelectedRow(), 5).toString());        
        mostrar.setEnabled(false);
                modificar.setEnabled(false);
                eliminar.setEnabled(false);
                limpiar.setEnabled(false);
                salir.setEnabled(false);
                crear.setEnabled(false);
                aceptar.setEnabled(true);
    }
}
    //desbloqueara los botonos luego de haber confirmado la actualizacion de informacion//
    void desbloquear(){
    crear.setEnabled(true);
    modificar.setEnabled(true);
    eliminar.setEnabled(true);
    aceptar.setEnabled(true);
    mostrar.setEnabled(true);
    limpiar.setEnabled(true);
    salir.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        IDP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        DP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        FA = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        FV = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        PP = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        crear = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        aceptar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        mostrar = new javax.swing.JButton();
        limpiar = new javax.swing.JButton();
        salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Book Antiqua", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 102));
        jLabel1.setText("DETALLES DE PRODUCTOS");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("IdProducto:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nombre del producto:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Descripcion del producto:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Fecha Adquisición:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Fecha Vencimiento:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Precio Producto");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IDP, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NP, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DP)
                            .addComponent(FA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PP))))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(DP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(FA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(FV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(PP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        crear.setText("Crear");
        crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearActionPerformed(evt);
            }
        });

        modificar.setText("Modificar");
        modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarActionPerformed(evt);
            }
        });

        aceptar.setText("Aceptar");
        aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarActionPerformed(evt);
            }
        });

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        mostrar.setText("Mostrar");
        mostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarActionPerformed(evt);
            }
        });

        limpiar.setText("Limpiar");
        limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarActionPerformed(evt);
            }
        });

        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(crear)
                        .addGap(18, 18, 18)
                        .addComponent(modificar)
                        .addGap(18, 18, 18)
                        .addComponent(aceptar)
                        .addGap(18, 18, 18)
                        .addComponent(eliminar)
                        .addGap(18, 18, 18)
                        .addComponent(mostrar)
                        .addGap(18, 18, 18)
                        .addComponent(limpiar)
                        .addGap(18, 18, 18)
                        .addComponent(salir)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(crear)
                    .addComponent(modificar)
                    .addComponent(aceptar)
                    .addComponent(eliminar)
                    .addComponent(mostrar)
                    .addComponent(limpiar)
                    .addComponent(salir))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearActionPerformed
       guardar();
    }//GEN-LAST:event_crearActionPerformed

    private void modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarActionPerformed
       cargar();
    }//GEN-LAST:event_modificarActionPerformed

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarActionPerformed
        actualizar();
    }//GEN-LAST:event_aceptarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
       Eliminar();
    }//GEN-LAST:event_eliminarActionPerformed

    private void mostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarActionPerformed
        mostrar();
    }//GEN-LAST:event_mostrarActionPerformed

    private void limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_limpiarActionPerformed

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Productos dialog = new Productos(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DP;
    private com.toedter.calendar.JDateChooser FA;
    private com.toedter.calendar.JDateChooser FV;
    private javax.swing.JTextField IDP;
    private javax.swing.JTextField NP;
    private javax.swing.JTextField PP;
    private javax.swing.JButton aceptar;
    private javax.swing.JButton crear;
    private javax.swing.JButton eliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiar;
    private javax.swing.JButton modificar;
    private javax.swing.JButton mostrar;
    private javax.swing.JButton salir;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
