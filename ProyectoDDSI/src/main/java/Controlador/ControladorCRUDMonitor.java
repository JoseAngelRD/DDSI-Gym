/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Vista.VistaCRUDMonitor;
import Vista.VistaMensajes;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.hibernate.Session;

/**
 *
 * @author JoseAngelRD
 */
public class ControladorCRUDMonitor implements ActionListener {

	private VistaCRUDMonitor vCRUDMonitor;
	private VistaMensajes vMensaje = new VistaMensajes();
	private int nMonitores;
	private Session sesion;
	private ActTablaMonitor actTMon;
	private Monitor monitorSeleccionado;

	// EN CASO DE QUERER INSERTAR UN MONITOR, ESTE CONSTRUCTOR
	public ControladorCRUDMonitor(String cod, int nMon, Session sesion, ActTablaMonitor actTMon) {
		this.actTMon = actTMon;
		this.sesion = sesion;
		nMonitores = nMon;
		vCRUDMonitor = new VistaCRUDMonitor();
		vCRUDMonitor.jTextFieldCodMonitor.setEditable(false);

		addListeners();
		vCRUDMonitor.jTextFieldCodMonitor.setText(cod);
		vCRUDMonitor.setTitle("Insertar Monitor");
		vCRUDMonitor.jButton1.setName("Insertar");
		vCRUDMonitor.setLocationRelativeTo(null);
		vCRUDMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		vCRUDMonitor.setResizable(false);
		vCRUDMonitor.setVisible(true);
	}

	// EN CASO DE QUERER ACTUALIZAR UN MONITOR, ESTE CONSTRUCTOR
	public ControladorCRUDMonitor(Monitor monitorSeleccionado, int nMon, Session sesion, ActTablaMonitor actTMon)
			throws ParseException {
		this.monitorSeleccionado = monitorSeleccionado;
		this.actTMon = actTMon;
		this.sesion = sesion;
		nMonitores = nMon;
		vCRUDMonitor = new VistaCRUDMonitor();
		vCRUDMonitor.jTextFieldCodMonitor.setEditable(false);
		vCRUDMonitor.setTitle("Actualizar Monitor");
		vCRUDMonitor.jButton1.setLabel("Actualizar");

		addListeners();
		vCRUDMonitor.jTextFieldCodMonitor.setText(monitorSeleccionado.getCodMonitor());
		vCRUDMonitor.jTextFieldNomMonitor.setText(monitorSeleccionado.getNombre());
		vCRUDMonitor.jTextFieldDNIMonitor.setText(monitorSeleccionado.getDni());
		vCRUDMonitor.jTextFieldTelfMonitor.setText(monitorSeleccionado.getTelefono());
		vCRUDMonitor.jTextFieldCorreoMonitor.setText(monitorSeleccionado.getCorreo());

		String formatoFecha = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		Date fecha = sdf.parse(this.monitorSeleccionado.getFechaEntrada());

		vCRUDMonitor.jDateChooserCalendario.setDate(fecha);
		vCRUDMonitor.jTextFieldNickMonitor.setText(monitorSeleccionado.getNick());

		vCRUDMonitor.setLocationRelativeTo(null);
		vCRUDMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		vCRUDMonitor.setResizable(false);
		vCRUDMonitor.setVisible(true);
	}

	private void addListeners() {
		vCRUDMonitor.jButton1.addActionListener((ActionListener) this); // insertar
		vCRUDMonitor.jButton2.addActionListener((ActionListener) this); // salir

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case "Insertar":
		case "Actualizar":
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

			if (vCRUDMonitor.jTextFieldNomMonitor.getText().length() == 0) {
				vMensaje.mensajeErrorNombre(vCRUDMonitor);
				break;
			}

			if (vCRUDMonitor.jTextFieldDNIMonitor.getText().length() != 9) {
				vMensaje.mensajeErrorDNI(vCRUDMonitor);
				break;
			}

			if (vCRUDMonitor.jTextFieldTelfMonitor.getText().length() != 9
					&& vCRUDMonitor.jTextFieldTelfMonitor.getText().length() != 0) {
				vMensaje.mensajeErrorTelefono(vCRUDMonitor);
				break;
			}

			String correo = vCRUDMonitor.jTextFieldCorreoMonitor.getText();

			if (!correo.contains("@") && !"".equals(correo)) {
				vMensaje.mensajeErrorCorreo(vCRUDMonitor);
				break;
			}

			if (vCRUDMonitor.jTextFieldNickMonitor.getText().length() > 6
					&& !"".equals(vCRUDMonitor.jTextFieldNickMonitor.getText())) {
				vMensaje.mensajeErrorNick(vCRUDMonitor);
				break;
			}

			String fechaString = "";

			Date fechaActual = new Date();
			Date fechaChooser = vCRUDMonitor.jDateChooserCalendario.getDate();

			if (fechaChooser.compareTo(fechaActual) > 0) {
				vMensaje.mensajeErrorFecha(vCRUDMonitor);
				break;
			} else if (fechaChooser != null) {
				fechaString = formatoFecha.format(fechaChooser);
			} else {
				vMensaje.mensajeErrorFecha(vCRUDMonitor);
				break;
			}

			Monitor nuevoM = new Monitor(vCRUDMonitor.jTextFieldCodMonitor.getText(),
					vCRUDMonitor.jTextFieldNomMonitor.getText(), vCRUDMonitor.jTextFieldDNIMonitor.getText(),
					vCRUDMonitor.jTextFieldTelfMonitor.getText(), vCRUDMonitor.jTextFieldCorreoMonitor.getText(),
					fechaString, vCRUDMonitor.jTextFieldNickMonitor.getText());

			try {
				MonitorDAO.insertarActualizarMonitor(sesion, nuevoM);
				vCRUDMonitor.dispose();
				nMonitores++;

				ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitoresConHQL((Session) sesion);

				actTMon.vaciarTablaMonitores();
				actTMon.rellenarTablaMonitores(lMonitores);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al insertar nuevo monitor:\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			break;

		case "Cancelar":
			vCRUDMonitor.dispose();
			break;
		}
	}

}
