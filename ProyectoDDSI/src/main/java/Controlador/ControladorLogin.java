/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.VistaLogin;

import Config.HibernateUtilMariaDB;
import Config.HibernateUtilOracle;
import Vista.VistaMensajes;
import Vista.VistaMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author usuario
 */
public class ControladorLogin implements ActionListener {

	private SessionFactory sessionFactory;

	private VistaMensajes vista = new VistaMensajes();
	private VistaLogin vLogin = new VistaLogin();
	private ControladorPrincipal controladorP;

	public ControladorLogin() throws ClassNotFoundException, SQLException {

		addListeners();
		vLogin.setLocationRelativeTo(null);
		vLogin.setVisible(true);

		vLogin.jComboBox1Servidores.setSelectedIndex(0);
	}

	private void addListeners() {
		vLogin.jButton2Salir.addActionListener(this);
		vLogin.jButton1Conectar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case "Conectar":
			try {
				conectarBD(vLogin.jComboBox1Servidores.getSelectedItem().toString());
				if (sessionFactory != null) {
					vLogin.dispose();
					controladorP = new ControladorPrincipal(sessionFactory);
				}
			} catch (SQLException ex) {
				Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
			}
			break;

		case "Salir":
			vLogin.dispose();
			System.exit(0);
			break;
		}
	}

	private void conectarBD(String servidor) {

		try {

			if (servidor.equals("Oracle")) {
				sessionFactory = HibernateUtilOracle.getSessionFactory();
			} else {
				sessionFactory = HibernateUtilMariaDB.getSessionFactory();
			}
			String text = "Conexion Correcta con Hibernate";
			vista.mensajeConsola(text);

		} catch (ExceptionInInitializerError e) {
			Throwable cause = e.getCause();
			vista.mensajeConsola("Error en la conexión. Revise el fichero .cfg.xml: ", cause.getMessage());
		}

	}

}
