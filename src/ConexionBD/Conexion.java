/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionBD;

import java.sql.*;

public class Conexion {
     private String db="final erix";
    private String user="root";
    private String Password="";
    private String url="jdbc:mysql://localhost/"+db;
    private Connection conn= null;
    
    //se especifican detatlles de la conexion a la BD//
    public Conexion(){
        this.url="jdbc:mysql://localhost:3306/"
                + "final erix?serverTunezone=UTC";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection(this.url, 
                    this.user, this.Password);
            if(conn!=null){
                System.out.println("Se ha conectado a la "
                        + "base de datos"+db+" Bienvenido");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }      
    }
    public Connection ObtenerConexion(){
        return conn;
    }
}
