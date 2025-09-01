/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package Aplicacion;

import Controlador.ControladorLogin;
import Controlador.ControladorPrincipal;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import Config.HibernateUtilOracle;
import Config.HibernateUtilMariaDB;
import Vista.VistaMensajes;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author usuario
 */
public class ProyectoDDSI {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		System.out.println("MENU DE LA CONEXION\n ---------------------------");
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());

		} catch (UnsupportedLookAndFeelException ex) {

			System.out.println("Error al aplicar LookAndFeel");
		}

		// 1 CONECTA CON ORACLE 2 CONECTA CON MARIADB

		ControladorLogin a = new ControladorLogin();

	}
}
