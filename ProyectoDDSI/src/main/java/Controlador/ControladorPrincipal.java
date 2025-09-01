/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.VistaMensajes;
import Vista.VistaMenu;
import Vista.VistaMonitores;
import Vista.VistaSocios;
import Vista.VistaVacio;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import Vista.VistaInsertarBajarActividad;

/**
 *
 * @author usuario
 */
public class ControladorPrincipal implements ActionListener {

	private SessionFactory abrirSesion;
	private Session sesion;
	private Transaction tr;
	private VistaMensajes vista = new VistaMensajes(), vSocios = new VistaMensajes();
	private VistaMenu vMenu = new VistaMenu();
	private VistaVacio pPrincipal = new VistaVacio();
	private VistaMonitores vMonitor = new VistaMonitores();
	private VistaSocios vSocio = new VistaSocios();
	private VistaInsertarBajarActividad vInsBajActSocio = new VistaInsertarBajarActividad();
	private ActTablaSocio actTSocio;
	private ActTablaMonitor actTMonitor;
	private ActTablaActividadesDeSocio actTActSocio;
	private ControladorCRUDMonitor cCRUDMon;
	private ControladorCRUDSocio cCRUDSoc;
	private int numeroMonitores = 0, numeroSocios = 0;

	ControladorPrincipal(SessionFactory sesion) throws SQLException {
		abrirSesion = sesion;
		this.sesion = sesion.openSession();
		tr = this.sesion.beginTransaction();
		actTMonitor = new ActTablaMonitor(vMonitor);
		actTSocio = new ActTablaSocio(vSocio);
		actTActSocio = new ActTablaActividadesDeSocio(vInsBajActSocio);

		addListeners();

		vMenu.getContentPane().setLayout(new CardLayout());
		vMenu.add(pPrincipal);
		vMenu.add(vMonitor);
		vMenu.add(vSocio);

		vMenu.setLocationRelativeTo(null);
		vMenu.setVisible(true);

		pPrincipal.setVisible(true);
		vMonitor.setVisible(false);
		vSocio.setVisible(false);

		// menu();
	}

	private void addListeners() {
		vMenu.jMenuItem1.addActionListener((ActionListener) this); // gestion monitores
		vMenu.jMenuItem3.addActionListener((ActionListener) this); // gestion socios
		vMenu.jMenuItem4.addActionListener((ActionListener) this); // salir
		vMenu.jMenuItem5.addActionListener((ActionListener) this); // limpiar pantalla

		vMonitor.jButton1.addActionListener(this); // insertar monitor
		vMonitor.jButton2.addActionListener(this); // eliminar monitor
		vMonitor.jButton3.addActionListener(this); // actualizar monitor

		vSocio.jButton1.addActionListener(this); // insertar socio
		vSocio.jButton2.addActionListener(this); // eliminar socio
		vSocio.jButton3.addActionListener(this); // actualizar socio
		vSocio.jButtonActSocio.addActionListener(this); // insert Act socio

		vSocio.jButtonBuscar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case "Gestion de Monitores":
			pPrincipal.setVisible(false);
			vSocio.setVisible(false);
			vMonitor.setVisible(true);

			this.sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			actTMonitor.actualizarEditable();
			actTMonitor.dibujarTablaMonitores();

			try {
				ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitoresConHQL((Session) sesion);
				numeroMonitores = lMonitores.size();
				actTMonitor.vaciarTablaMonitores();
				actTMonitor.rellenarTablaMonitores(lMonitores);

				tr.commit();

			} catch (Exception ex) {
				tr.rollback();
				vista.mensajeConsola("Error en la petición de monitores", ex.getMessage());
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Gestion de Socios":
			pPrincipal.setVisible(false);
			vMonitor.setVisible(false);
			vSocio.setVisible(true);
			vSocio.jComboBoxTipoBusqueda.setSelectedIndex(0);

			this.sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			actTSocio.actualizarEditable();
			actTSocio.dibujarTablaSocios();

			try {
				ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);
				numeroSocios = lSocios.size();
				actTSocio.vaciarTablaSocios();
				actTSocio.rellenarTablaSocios(lSocios);

				tr.commit();

			} catch (Exception ex) {
				tr.rollback();
				vista.mensajeConsola("Error en la petición de socios", ex.getMessage());
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}
			break;

		case "Limpiar Pantalla":
			vMonitor.setVisible(false);
			vSocio.setVisible(false);
			pPrincipal.setVisible(true);

			break;

		case "Nuevo Monitor":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				String cod = "";
				boolean codYaAsignado = false;
				ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitoresConHQL((Session) sesion);
				for (int i = 1; i <= lMonitores.size(); i++) {
					codYaAsignado = false;
					if (i < 10) {
						cod = "M00" + i;
					} else if (i < 100) {
						cod = "M0" + i;
					} else {
						cod = "M" + i;
					}
					for (int j = 1; j <= lMonitores.size(); j++) {
						if (cod.equals(lMonitores.get(i - 1).getCodMonitor())) {
							codYaAsignado = true;
							break;
						}
					}
					if (codYaAsignado == false) {
						break;
					}
				}
				if (codYaAsignado == true) {
					if (numeroMonitores + 1 < 10) {
						cod = "M00" + (numeroMonitores + 1);
					} else if (numeroMonitores + 1 < 100) {
						cod = "M0" + (numeroMonitores + 1);
					} else if (numeroMonitores + 1 < 1000) {
						cod = "M" + (numeroMonitores + 1);
					}
				}
				cCRUDMon = new ControladorCRUDMonitor(cod, numeroMonitores, sesion, actTMonitor);
				tr.commit();
			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al insertar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Baja Monitor":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				int selectedRow = vMonitor.jTable1.getSelectedRow();
				if (selectedRow != -1) {

					String nombreSeleccionado = vMonitor.jTable1.getValueAt(selectedRow, 1).toString();
					String codSeleccionado = vMonitor.jTable1.getValueAt(selectedRow, 0).toString();
					int eliminar = vista.muestraEliminarPersona(vMonitor, nombreSeleccionado);
					if (eliminar == 0) {
						MonitorDAO.bajaMonitor(sesion, codSeleccionado);
					}
					tr.commit();

					ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitoresConHQL((Session) sesion);

					actTMonitor.vaciarTablaMonitores();
					actTMonitor.rellenarTablaMonitores(lMonitores);
				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al eliminar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Actualizacion de Monitor":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				int selectedRow = vMonitor.jTable1.getSelectedRow();
				if (selectedRow != -1) {

					String cod = vMonitor.jTable1.getValueAt(selectedRow, 0).toString();
					String nom = vMonitor.jTable1.getValueAt(selectedRow, 1).toString();
					String DNI = vMonitor.jTable1.getValueAt(selectedRow, 2).toString();
					String tlf;
					if (vMonitor.jTable1.getValueAt(selectedRow, 3) != null) {
						tlf = vMonitor.jTable1.getValueAt(selectedRow, 3).toString();
					} else {
						tlf = "";
					}

					String corr;
					if (vMonitor.jTable1.getValueAt(selectedRow, 4) != null) {
						corr = vMonitor.jTable1.getValueAt(selectedRow, 4).toString();
					} else {
						corr = "";
					}

					String fech = vMonitor.jTable1.getValueAt(selectedRow, 5).toString();

					String nick;
					if (vMonitor.jTable1.getValueAt(selectedRow, 6) != null) {
						nick = vMonitor.jTable1.getValueAt(selectedRow, 6).toString();
					} else {
						nick = "";
					}

					Monitor actualizarMonitor = new Monitor(cod, nom, DNI, tlf, corr, fech, nick);
					cCRUDMon = new ControladorCRUDMonitor(actualizarMonitor, numeroMonitores, sesion, actTMonitor);

					tr.commit();

					ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitoresConHQL((Session) sesion);

					actTMonitor.vaciarTablaMonitores();
					actTMonitor.rellenarTablaMonitores(lMonitores);
				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al actualizar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Nuevo Socio":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				String cod = "";
				boolean codYaAsignado = false;
				ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);
				numeroSocios = lSocios.size();
				for (int i = 1; i <= lSocios.size(); i++) {
					codYaAsignado = false;
					if (i < 10) {
						cod = "S00" + i;
					} else if (i < 100) {
						cod = "S0" + i;
					} else {
						cod = "S" + i;
					}
					for (int j = 1; j <= lSocios.size(); j++) {
						if (cod.equals(lSocios.get(i - 1).getNumeroSocio())) {
							codYaAsignado = true;
							break;
						}
					}
					if (codYaAsignado == false) {
						break;
					}
				}
				if (codYaAsignado == true) {
					if (numeroSocios + 1 < 10) {
						cod = "S00" + (numeroSocios + 1);
					} else if (numeroSocios + 1 < 100) {
						cod = "S0" + (numeroSocios + 1);
					} else if (numeroSocios + 1 < 1000) {
						cod = "S" + (numeroSocios + 1);
					}
				}
				cCRUDSoc = new ControladorCRUDSocio(cod, numeroSocios, sesion, actTSocio);
				tr.commit();
			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al insertar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Baja Socio":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				int selectedRow = vSocio.jTable1.getSelectedRow();
				if (selectedRow != -1) {

					String nombreSeleccionado = vSocio.jTable1.getValueAt(selectedRow, 1).toString();
					String codSeleccionado = vSocio.jTable1.getValueAt(selectedRow, 0).toString();
					int eliminar = vista.muestraEliminarPersona(vSocio, nombreSeleccionado);
					if (eliminar == 0) {
						SocioDAO.bajaSocio(sesion, codSeleccionado);
					}
					tr.commit();

					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);

					actTSocio.vaciarTablaSocios();
					actTSocio.rellenarTablaSocios(lSocios);
				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al eliminar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Actualizacion de Socio":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				int selectedRow = vSocio.jTable1.getSelectedRow();
				if (selectedRow != -1) {

					String cod = vSocio.jTable1.getValueAt(selectedRow, 0).toString();
					String nom = vSocio.jTable1.getValueAt(selectedRow, 1).toString();
					String DNI = vSocio.jTable1.getValueAt(selectedRow, 2).toString();

					String telefono;
					if (vSocio.jTable1.getValueAt(selectedRow, 3) != null) {
						telefono = vSocio.jTable1.getValueAt(selectedRow, 3).toString();
					} else {
						telefono = "";
					}

					String corr;
					if (vSocio.jTable1.getValueAt(selectedRow, 4) != null) {
						corr = vSocio.jTable1.getValueAt(selectedRow, 4).toString();
					} else {
						corr = "";
					}

					String fechEntrada = vSocio.jTable1.getValueAt(selectedRow, 5).toString();

					Character cat = vSocio.jTable1.getValueAt(selectedRow, 6).toString().charAt(0);

					String fechNac;
					if (vSocio.jTable1.getValueAt(selectedRow, 7) != null) {
						fechNac = vSocio.jTable1.getValueAt(selectedRow, 7).toString();
					} else {
						fechNac = "";
					}

					Socio actualizarSocio = new Socio(cod, nom, DNI, fechNac, telefono, corr, fechEntrada, cat);
					cCRUDSoc = new ControladorCRUDSocio(actualizarSocio, numeroSocios, sesion, actTSocio);

					tr.commit();

					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);

					actTSocio.vaciarTablaSocios();
					actTSocio.rellenarTablaSocios(lSocios);
				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al actualizar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Actividades Socio":

			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				int selectedRow = vSocio.jTable1.getSelectedRow();
				if (selectedRow != -1) {

					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);
					String cod = vSocio.jTable1.getValueAt(selectedRow, 0).toString();
					boolean encontrado = false;
					int i = 0;
					Socio socAux = new Socio();

					while (i < lSocios.size() && encontrado == false) {
						if (lSocios.get(i).getNumeroSocio().equals(cod)) {
							socAux = lSocios.get(i);
							encontrado = true;
						} else
							i++;
					}

					cCRUDSoc = new ControladorCRUDSocio(socAux, sesion, actTActSocio, vInsBajActSocio);

					tr.commit();

				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al actualizar:\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Buscar":
			sesion = abrirSesion.openSession();
			tr = this.sesion.beginTransaction();

			try {
				if (!"".equals(vSocio.jTextFieldBuscador.getText())) {
					String tipo = vSocio.jComboBoxTipoBusqueda.getSelectedItem().toString();
					String busqueda = vSocio.jTextFieldBuscador.getText();

					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);

					actTSocio.vaciarTablaSocios();
					actTSocio.rellenarTablaSociosBusqueda(lSocios, tipo, busqueda);
				} else {
					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);

					actTSocio.vaciarTablaSocios();
					actTSocio.rellenarTablaSocios(lSocios);
				}

			} catch (Exception ex) {
				tr.rollback();
				JOptionPane.showMessageDialog(null, "Error al buscar socio:\n" + ex, "Error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (sesion != null && sesion.isOpen()) {
					sesion.close();
				}
			}

			break;

		case "Salir":
			vMenu.dispose();
			System.exit(0);
			break;

		}
	}

	private void menu() throws SQLException {

		Scanner lectura = new Scanner(System.in);

		int opcion;

		do {
			System.out.println("MENU DE LA CONEXION\n -------------------------- \n\nOPCIONES:");
			System.out.println("1. Listado de Socios HQL");
			System.out.println("2. Listado de Socios SQL Nativo");
			System.out.println("3. Listado de Socios ConsultaNombrada (no va)");
			System.out.println("4. Nombre y teléfono de los socios");
			System.out.println("5. Nombre y categoría de los socios");
			System.out.println("6. Responsable de una actividad");
			System.out.println("7. Socios de una actividad");
			System.out.println("8. Alta de un socio.");
			System.out.println("9. Baja de un socio");
			System.out.println("10. Actualización de la categoría de un socio");
			System.out.println("11. Inscripción de un socio en una actividad");
			System.out.println("12. Baja de un socio de una actividad.");
			System.out.println("13. Listado de socios inscritos en una actividad.");
			System.out.println("14. Listado de actividades de un socio.");
			System.out.println("15. Salir.");

			System.out.println("----------------------------\n");

			opcion = lectura.nextInt();

			switch (opcion) {
			case 1: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);
					VistaMensajes.muestraSocios(lSocios);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}
			case 2: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {

					ArrayList<Socio> lSocios = SocioDAO.listaSociosSQL((Session) sesion);
					vSocios.muestraSocios(lSocios);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}
			case 3: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {

					ArrayList<Socio> lSocios = SocioDAO.listaSociosConsultaNombrada((Session) sesion);
					vSocios.muestraSocios(lSocios);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}
			case 4: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {

					ArrayList<Object[]> lNomTlf = SocioDAO.listaNombreTelefono((Session) sesion);

					vSocios.mensajeSocioTelefono(lNomTlf);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}
			case 5: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Indica la categoria a mostrar: ");
					String cat = lectura.nextLine();

					ArrayList<Socio> lNomTlf = SocioDAO.listaSociosHQL((Session) sesion);
					// vSocios.muestraSocios(lSocios);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}

			case 6: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					ArrayList<Actividad> lActividades = ActividadDAO.ResponsableActividad((Session) sesion);
					vSocios.muestraActividades(lActividades);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;
			}

			case 7: {
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					// PEDIR LO QUE QUIERO BUSCAR
					String categoria = "";
					ArrayList<Object[]> lSocios = SocioDAO.listaNombreCategoriaSocios((Session) sesion, categoria);
					// vSocios.muestraSocios(lSocios);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en la petición de socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
			}
				break;
			case 8:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					Socio socioNuevo = new Socio("S999", "Nuevo Socio", "11222333F", "20/10/2022", 'A');
					Actividad actividad = sesion.get(Actividad.class, "AC01");
					actividad.altaSocio(socioNuevo);
					sesion.saveOrUpdate(socioNuevo); // es necesario porque es un nuevo socio en la BD
					sesion.saveOrUpdate(actividad);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en alta socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 9:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					Socio socio = sesion.get(Socio.class, "S999");
					Actividad actividad = sesion.get(Actividad.class, "AC03");
					actividad.bajaSocio(socio);
					sesion.saveOrUpdate(actividad);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en baja socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 10:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Inserte la ID del socio a actualizar: ");
					String id = lectura.nextLine();
					System.out.println("Inserte la Categoria a actualizar: ");
					char cat = lectura.next().charAt(0);
					System.out.println("Inserte el nuevo VALOR a actualizar: ");
					String valor = lectura.nextLine();

					Socio socio = sesion.get(Socio.class, id);
					socio.setCategoria(cat);

					sesion.saveOrUpdate(socio);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en actualizar socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 11:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Inserte la ID del socio a inscribir: ");
					String idSocio = lectura.nextLine();
					System.out.println("Inserte el codigo de la actividad: ");
					String idAct = lectura.nextLine();

					Socio socio = sesion.get(Socio.class, idSocio);
					Actividad actividad = sesion.get(Actividad.class, idAct);
					actividad.altaSocio(socio);

					sesion.saveOrUpdate(actividad);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en inscribir socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 12:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Inserte la ID del socio a dar de baja: ");
					String idSocio = lectura.nextLine();
					System.out.println("Inserte el codigo de la actividad: ");
					String idAct = lectura.nextLine();

					Socio socio = sesion.get(Socio.class, idSocio);
					Actividad actividad = sesion.get(Actividad.class, idAct);
					actividad.bajaSocio(socio);

					sesion.saveOrUpdate(actividad);

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error dar de baja socios", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 13:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Inserte el codigo de la actividad para ver los socios: ");
					String idAct = lectura.nextLine();

					Actividad actividad = sesion.get(Actividad.class, idAct);
					Set<Socio> s = actividad.getSocios();

					Iterator<Socio> iterator = s.iterator();
					while (iterator.hasNext()) {
						System.out.println(iterator.next().getNombre() + iterator.next().getTelefono());
					}

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en ver socios de una actividad", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 14:
				this.sesion = abrirSesion.openSession();
				tr = this.sesion.beginTransaction();
				try {
					System.out.println("Inserte la ID del socio para ver sus actividades: ");
					String idSocio = lectura.nextLine();

					Socio socio = sesion.get(Socio.class, idSocio);
					Set<Actividad> act = socio.getActividades();

					Iterator<Actividad> iterator = act.iterator();
					while (iterator.hasNext()) {
						System.out.println(iterator.next().getNombre() + iterator.next().getPrecioBaseMes());
					}

					tr.commit();

				} catch (Exception e) {
					tr.rollback();
					vista.mensajeConsola("Error en ver actividades de un socio", e.getMessage());
				} finally {
					if (sesion != null && sesion.isOpen()) {
						sesion.close();
					}
				}
				break;

			case 15:
				break;

			}
		} while (opcion != 15);

	}

}
