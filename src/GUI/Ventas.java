/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import ConexionBD.*;

public class Ventas extends javax.swing.JDialog {

    /**
     * Creates new form Ventas
     */
    public Ventas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Conexion basedatos = new Conexion();
        Connection conn;
        conn= basedatos.ObtenerConexion();
    }
    
    //se crea el metodo  limpiar que enviara texto vacio a todos los campos //
    public void Limpiar(){
        IDV.setText("");
        NP1.setText("");
        PU1.setText("");
        CP.setText("");
        NIT.setText("");
        NC.setText("");
        PT.setText("");
        DE.setText("");  
    }

    //metodo guardar que enviara mis datos de los campos hacia la Base de Datos//
   public void guardar(){
       int cod;
       String idve,nombrepro,preciouni,cantidad1,nit,nombcl;
       idve= IDV.getText();
       nombrepro= NP1.getText();
       preciouni= PU1.getText();
       cantidad1=CP.getText();
       nit=NIT.getText();
       nombcl=NC.getText();
       
       if(idve.equals("")|| nombrepro.equals("")||nombrepro.equals("")||preciouni.equals("")|| nit.equals("")||
               nombcl.equals("")){
           System.out.println("Porfavor ingrese todos los "
                   + "datos de los camos");
       }
       else {
           try{
               Conexion basedatos= new Conexion();
               Connection conn;
               conn= basedatos.ObtenerConexion();
               PreparedStatement ps1= null;
               ResultSet consulta = null;
               
               String sql1="insert ventas (idventa, nombreproducto, preciounitario, cantidad, preciototal,"
                       + " nitcliente,nombrecliente, descuento, estado) values (?,?,?,?,?,?,?,?,1)";
               
               ps1=conn.prepareStatement(sql1);
               String id= IDV.getText();
               ps1.setString(1, id);
               String nomb= NP1.getText();
               ps1.setString(2, nomb);
               String prec= PU1.getText();
               ps1.setString(3, prec);
               String cant= CP.getText();
               ps1.setString(4, cant);
               String pret= PT.getText();
               ps1.setString(5, pret);
               String nitc= NIT.getText();
               ps1.setString(6, nitc);
               String nombc= NC.getText();
               ps1.setString(7, nombc);
               String descuento1= DE.getText();
               ps1.setString(8, descuento1);

               ps1.execute();

            JOptionPane.showMessageDialog(this, "Los datos han sido almacenados de forma correcta");
               Limpiar();
               mostrar();
           }catch(Exception e){
               JOptionPane.showMessageDialog(this, "Error al almacenar la informacion");
           }
       }
   }
   //metodo Proceso que servira para segun los datos ingresados de precio y cantidad que consumira el usuario//
   //se procedera a calcular el total y si aplica o no aplica al descuento//
   public void Proceso(){
       String valor1= CP.getText();
       String valor2= PU1.getText();
           int dato= Integer.valueOf (valor2);
           int dato1= Integer.valueOf (valor1);
           int total=dato*dato1;
           PT.setText(String.valueOf(total));   
           if(total>500){
               double desc=total*0.10;
               DE.setText(String.valueOf(desc));
           }else{
               DE.setText("0");
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
                   consulta= comando.executeQuery("select idventa,nombreproducto, preciounitario, cantidad, "
                       + "preciototal, nitcliente,nombrecliente,descuento from ventas where estado !=0;");
                   DefaultTableModel modelo= new DefaultTableModel();
                   this.tabla2.setModel(modelo);
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
    //metodo eliminar que enviara codigo de estado 0 al registro correspondiente que se haya seleccionado//
   public void Eliminar (){
    int filInicio= tabla2.getSelectedRow();
    int numfila= tabla2.getSelectedRowCount();
    
    ArrayList<String>listapersona= new ArrayList<>();
    String cod= null;
    
    if(filInicio>=0){
        for(int i=0; i<numfila; i++){
            cod= String.valueOf(tabla2.getValueAt(i+filInicio,0));
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
                     ps= conn.prepareStatement("UPDATE ventas SET estado= 0 WHERE idventa="+cod);
                     
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
   public void Actualizar (){
    int filInicio= tabla2.getSelectedRow();
    int numfila= tabla2.getSelectedRowCount();
    
    ArrayList<String>listapersona= new ArrayList<>();
    String cod= null;
    
    if(filInicio>=0){
        for(int i=0; i<numfila; i++){
            cod= String.valueOf(tabla2.getValueAt(i+filInicio,0));
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
                     
                     ps= conn.prepareStatement("UPDATE ventas SET idventa=?, nombreproducto=?, preciounitario=?, cantidad=?,"
                             + " preciototal=?, nitcliente=?, nombrecliente=?,descuento=? WHERE idventa="+cod);
                     ps.setString(1, IDV.getText());
                     ps.setString(2, NP1.getText());
                     ps.setString(3, PU1.getText());
                     ps.setString(4, CP.getText());
                     ps.setString(5, PT.getText());
                     ps.setString(6, NIT.getText());
                     ps.setString(7, NC.getText());
                     ps.setString(8, DE.getText());
                    
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
   // metodo cargar se encargara de enviar los datos de la tabla hacia los campos para permitir modificarlos
    public void cargar(){
    if(tabla2.getSelectedRowCount()>0){
        IDV.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 0).toString());
        NP1.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 1).toString());
        PU1.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 2).toString());
        CP.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 3).toString());   
        PT.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 4).toString());  
        NIT.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 5).toString());  
        NC.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 6).toString());  
        DE.setText(tabla2.getValueAt(tabla2.getSelectedRow(), 7).toString());  
        mostrar1.setEnabled(false);
                modificar1.setEnabled(false);
                eliminar1.setEnabled(false);
                limpiar1.setEnabled(false);
                salir1.setEnabled(false);
                crear1.setEnabled(false);
                aceptar1.setEnabled(true);
    }
}
    //metodo cargar enviara los datos de mi seleccion en la tabla hacia mis campos para poder modificarlos//
    void desbloquear(){
    crear1.setEnabled(true);
    modificar1.setEnabled(true);
    eliminar1.setEnabled(true);
    aceptar1.setEnabled(true);
    mostrar1.setEnabled(true);
    limpiar1.setEnabled(true);
    salir1.setEnabled(true);
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
        IDV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NP1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PU1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        CP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        NC = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        NIT = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        PT = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        DE = new javax.swing.JTextField();
        procesar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla2 = new javax.swing.JTable();
        crear1 = new javax.swing.JButton();
        modificar1 = new javax.swing.JButton();
        aceptar1 = new javax.swing.JButton();
        eliminar1 = new javax.swing.JButton();
        mostrar1 = new javax.swing.JButton();
        limpiar1 = new javax.swing.JButton();
        salir1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Bodoni MT", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 204));
        jLabel1.setText("SISTEMA DE VENTAS");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("ID de Venta:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nombre del Producto:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Precio Unitario:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Cantidad:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Nombre del Cliente:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Nit del Cliente:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Precio Total:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Descuento:");

        procesar.setText("Procesar");
        procesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                procesarActionPerformed(evt);
            }
        });

        jLabel10.setText("Presione procesar para determinar");

        jLabel11.setText("el precio total y el descuento");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PT, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(DE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(procesar)
                        .addGap(32, 32, 32))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 187, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(PT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(DE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(procesar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(54, 54, 54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(IDV, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(NP1)
                    .addComponent(PU1)
                    .addComponent(CP))
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(NIT, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(NC))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(62, 62, 62))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(NP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(PU1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(CP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabla2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabla2);

        crear1.setText("Crear");
        crear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crear1ActionPerformed(evt);
            }
        });

        modificar1.setText("Modificar");
        modificar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar1ActionPerformed(evt);
            }
        });

        aceptar1.setText("Aceptar");
        aceptar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar1ActionPerformed(evt);
            }
        });

        eliminar1.setText("Eliminar");
        eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminar1ActionPerformed(evt);
            }
        });

        mostrar1.setText("Mostrar");
        mostrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrar1ActionPerformed(evt);
            }
        });

        limpiar1.setText("Limpiar");
        limpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiar1ActionPerformed(evt);
            }
        });

        salir1.setText("Salir");
        salir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(crear1)
                        .addGap(18, 18, 18)
                        .addComponent(modificar1)
                        .addGap(18, 18, 18)
                        .addComponent(aceptar1)
                        .addGap(18, 18, 18)
                        .addComponent(eliminar1)
                        .addGap(18, 18, 18)
                        .addComponent(mostrar1)
                        .addGap(18, 18, 18)
                        .addComponent(limpiar1)
                        .addGap(18, 18, 18)
                        .addComponent(salir1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(crear1)
                    .addComponent(modificar1)
                    .addComponent(aceptar1)
                    .addComponent(eliminar1)
                    .addComponent(mostrar1)
                    .addComponent(limpiar1)
                    .addComponent(salir1))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void crear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crear1ActionPerformed
       guardar();
    }//GEN-LAST:event_crear1ActionPerformed

    private void modificar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar1ActionPerformed
        cargar();
    }//GEN-LAST:event_modificar1ActionPerformed

    private void aceptar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptar1ActionPerformed
       Actualizar();
    }//GEN-LAST:event_aceptar1ActionPerformed

    private void eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminar1ActionPerformed
       Eliminar();
    }//GEN-LAST:event_eliminar1ActionPerformed

    private void mostrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrar1ActionPerformed
        mostrar();
    }//GEN-LAST:event_mostrar1ActionPerformed

    private void limpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiar1ActionPerformed
        Limpiar();
    }//GEN-LAST:event_limpiar1ActionPerformed

    private void salir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salir1ActionPerformed
         System.exit(0);
    }//GEN-LAST:event_salir1ActionPerformed

    private void procesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_procesarActionPerformed
        Proceso();
    }//GEN-LAST:event_procesarActionPerformed

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
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Ventas dialog = new Ventas(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField CP;
    private javax.swing.JTextField DE;
    private javax.swing.JTextField IDV;
    private javax.swing.JTextField NC;
    private javax.swing.JTextField NIT;
    private javax.swing.JTextField NP1;
    private javax.swing.JTextField PT;
    private javax.swing.JTextField PU1;
    private javax.swing.JButton aceptar1;
    private javax.swing.JButton crear1;
    private javax.swing.JButton eliminar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiar1;
    private javax.swing.JButton modificar1;
    private javax.swing.JButton mostrar1;
    private javax.swing.JButton procesar;
    private javax.swing.JButton salir1;
    private javax.swing.JTable tabla2;
    // End of variables declaration//GEN-END:variables
}
