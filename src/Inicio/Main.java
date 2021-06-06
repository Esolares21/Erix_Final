/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;
import GUI.JPrincipal;
/**
 *
 * @author Erix
 */
public class Main {

   //se procede a mandar a mostrar el JPrincipal
    public static void main(String[] args) {
       JPrincipal frmprincipal = new JPrincipal();
		frmprincipal.setExtendedState(frmprincipal.MAXIMIZED_BOTH);
		frmprincipal.show();
    }
    
}
