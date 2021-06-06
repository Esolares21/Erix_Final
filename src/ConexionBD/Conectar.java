/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionBD;

import java.sql.*;
import javax.swing.*;
import java.sql.DriverManager;

public class Conectar {
     Connection conect= null;
      public Connection conexion(){
       try{
           // carga del Driver de MySQL
           Class.forName("com.mysql..jdbc.Driver");
           conect=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/final erix");
       }catch(Exception e){
           
       }
       return conect;
   }
}
