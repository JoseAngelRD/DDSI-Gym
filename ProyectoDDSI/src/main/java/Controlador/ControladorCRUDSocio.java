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
import Vista.VistaCRUDMonitor;
import Vista.VistaCRUDSocio;
import Vista.VistaInsertarBajarActividad;
import Vista.VistaMensajes;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
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

public class ControladorCRUDSocio implements ActionListener {

	private VistaCRUDSocio vCRUDSocio;
	private VistaInsertarBajarActividad vInsBajSocio;
	private VistaMensajes vMensaje = new VistaMensajes();
	private int nSocios;
	private Session sesion;
	private ActTablaSocio actTSoc;
	private ActTablaActividadesDeSocio actTActdeSocio;
	private Socio socioSeleccionado;

	// EN CASO DE QUERER INSERTAR UN SOCIO, ESTE CONSTRUCTOR
	public ControladorCRUDSocio(String cod, int nSoc, Session sesion, ActTablaSocio actTSoc) {
		this.actTSoc = actTSoc;
		this.sesion = sesion;
		nSocios = nSoc;
		this.vInsBajSocio = new VistaInsertarBajarActividad();
		vCRUDSocio = new VistaCRUDSocio();
		vCRUDSocio.jTextFieldCodSocio.setEditable(false);

		addListeners();
		vCRUDSocio.jTextFieldCodSocio.setText(cod);
		vCRUDSocio.setTitle("Insertar Socio");
		vCRUDSocio.jButton1.setName("Insertar");
		vCRUDSocio.setLocationRelativeTo(null);
		vCRUDSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		vCRUDSocio.setResizable(false);
		vCRUDSocio.setVisible(true);
	}

	// EN CASO DE QUERER ACTUALIZAR UN SOCIO, ESTE CONSTRUCTOR
	public ControladorCRUDSocio(Socio socioSeleccionado, int nSoc, Session sesion, ActTablaSocio actTSoc)
			throws ParseException {
		this.socioSeleccionado = socioSeleccionado;
		this.actTSoc = actTSoc;
		this.sesion = sesion;
		nSocios = nSoc;
		this.vInsBajSocio = new VistaInsertarBajarActividad();
		vCRUDSocio = new VistaCRUDSocio();
		vCRUDSocio.jTextFieldCodSocio.setEditable(false);
		vCRUDSocio.setTitle("Actualizar Socio");
		vCRUDSocio.jButton1.setLabel("Actualizar");

		String formatoFecha = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);

		addListeners();
		vCRUDSocio.jTextFieldCodSocio.setText(socioSeleccionado.getNumeroSocio());
		vCRUDSocio.jTextFieldNomSocio.setText(socioSeleccionado.getNombre());
		vCRUDSocio.jTextFieldDNISocio.setText(socioSeleccionado.getDni());
		vCRUDSocio.jTextFieldTelfSocio.setText(socioSeleccionado.getTelefono());
		vCRUDSocio.jTextFieldCorreoSocio.setText(socioSeleccionado.getCorreo());

		Date fechaNac;
		if (!"".equals(this.socioSeleccionado.getFechaNacimiento())) {
			fechaNac = sdf.parse(this.socioSeleccionado.getFechaNacimiento());
			vCRUDSocio.jDateChooserNac.setDate(fechaNac);
		}

		Date fechaEntrada = sdf.parse(this.socioSeleccionado.getFechaEntrada());
		vCRUDSocio.jDateChooserEntrada.setDate(fechaEntrada);

		vCRUDSocio.jTextFieldCategoriaSocio.setText(socioSeleccionado.getCategoria().toString());

		vCRUDSocio.setLocationRelativeTo(null);
		vCRUDSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		vCRUDSocio.setResizable(false);
		vCRUDSocio.setVisible(true);
	}

	// EN CASO DE QUERER INSERTAR O QUITAR UNA ACTIVIDAD
	public ControladorCRUDSocio(Socio socioSeleccionado, Session sesion, ActTablaActividadesDeSocio actTActdeSocio,
			VistaInsertarBajarActividad vInsBajSocio) throws ParseException, Exception {
		this.socioSeleccionado = socioSeleccionado;
		this.actTActdeSocio = actTActdeSocio;
		this.sesion = sesion;

		vCRUDSocio = new VistaCRUDSocio();
		this.vInsBajSocio = vInsBajSocio;
		vInsBajSocio.setTitle("Actividades del Socio");

		this.actTActdeSocio.actualizarEditable();
		this.actTActdeSocio.dibujarTablaActSocios();

		this.actTActdeSocio.vaciarTablaSocios();
		this.actTActdeSocio.rellenarTablaActSocios(socioSeleccionado.getActividades(), this.sesion);

		addListeners();

		vInsBajSocio.setLocationRelativeTo(null);
		vInsBajSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		vInsBajSocio.setResizable(false);
		vInsBajSocio.setVisible(true);
	}

	private void addListeners() {
		vCRUDSocio.jButton1.addActionListener((ActionListener) this); // insertar
		vCRUDSocio.jButton2.addActionListener((ActionListener) this); // salir

		vInsBajSocio.jButtonInscribir.addActionListener((ActionListener) this); // inscribir en act
		vInsBajSocio.jButtonBaja.addActionListener((ActionListener) this); // dar baja en act

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case "Insertar":
		case "Actualizar":
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

			if (vCRUDSocio.jTextFieldNomSocio.getText().length() == 0) {
				vMensaje.mensajeErrorNombre(vCRUDSocio);
				break;
			}

			if (vCRUDSocio.jTextFieldDNISocio.getText().length() != 9) {
				vMensaje.mensajeErrorDNI(vCRUDSocio);
				break;
			}

			String fechaNacString = "";

			Date fechaActual = new Date();
			Date fechaNacChooser = vCRUDSocio.jDateChooserNac.getDate();

			boolean mayorEdad = false;
			if (fechaNacChooser != null)
				mayorEdad = CalcularMayorDeEdad(fechaActual, fechaNacChooser);

			if (fechaNacChooser != null) {
				if (!mayorEdad) {
					vMensaje.mensajeErrorFecha(vCRUDSocio);
					break;
				} else {
					fechaNacString = formatoFecha.format(fechaNacChooser);
				}
			}

			if (vCRUDSocio.jTextFieldTelfSocio.getText().length() != 9
					&& vCRUDSocio.jTextFieldTelfSocio.getText().length() != 0) {
				vMensaje.mensajeErrorTelefono(vCRUDSocio);
				break;
			}

			String correo = vCRUDSocio.jTextFieldCorreoSocio.getText();

			if (!correo.contains("@") && !"".equals(correo)) {
				vMensaje.mensajeErrorCorreo(vCRUDSocio);
				break;
			}

			String fechaString = "";

			Date fechaChooser = vCRUDSocio.jDateChooserEntrada.getDate();

			if (fechaChooser == null) {
				vMensaje.mensajeErrorFecha(vCRUDSocio);
				break;
			} else if (fechaChooser.compareTo(fechaActual) > 0) {
				vMensaje.mensajeErrorFecha(vCRUDSocio);
				break;
			} else
				fechaString = formatoFecha.format(fechaChooser);

			if (vCRUDSocio.jTextFieldCategoriaSocio.getText().length() > 1
					|| "".equals(vCRUDSocio.jTextFieldCategoriaSocio.getText())) {
				vMensaje.mensajeErrorCategoria(vCRUDSocio);
				break;
			}

			Character caracter = vCRUDSocio.jTextFieldCategoriaSocio.getText().charAt(0);

			Socio nuevoS = new Socio(vCRUDSocio.jTextFieldCodSocio.getText(), vCRUDSocio.jTextFieldNomSocio.getText(),
					vCRUDSocio.jTextFieldDNISocio.getText(), fechaNacString, vCRUDSocio.jTextFieldTelfSocio.getText(),
					vCRUDSocio.jTextFieldCorreoSocio.getText(), fechaString, caracter);

			try {
				SocioDAO.insertarActualizarSocio(sesion, nuevoS);
				vCRUDSocio.dispose();
				nSocios++;

				ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL((Session) sesion);

				actTSoc.vaciarTablaSocios();
				actTSoc.rellenarTablaSocios(lSocios);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al insertar nuevo socio:\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			break;

		case "Cancelar":
			vCRUDSocio.dispose();
			break;

		case "Inscribir":

			String donde = vInsBajSocio.jComboBoxIns.getSelectedItem().toString();

			try {
				SocioDAO.inscribirActividad(sesion, socioSeleccionado.getNumeroSocio(), donde);
				actTActdeSocio.vaciarTablaSocios();
				actTActdeSocio.rellenarTablaActSocios(socioSeleccionado.getActividades(), this.sesion);

				JOptionPane.showMessageDialog(null, "Socio inscrito con exito\n", "Ok",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al inscribir el socio\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			break;

		case "Dar Baja":

			String DeDonde = vInsBajSocio.jComboBoxBaja.getSelectedItem().toString();

			try {
				SocioDAO.bajaActividad(sesion, socioSeleccionado.getNumeroSocio(), DeDonde);
				actTActdeSocio.vaciarTablaSocios();
				actTActdeSocio.rellenarTablaActSocios(socioSeleccionado.getActividades(), this.sesion);

				JOptionPane.showMessageDialog(null, "Socio dado de baja con exito\n", "Ok",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al dar de baja el socio\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			break;
		}
	}

	private static boolean CalcularMayorDeEdad(Date fechaActual, Date fechaNacChooser) {
		Date fechaHace18anios = restarAnios(fechaActual, 18);

		if (esMayorDeEdad(fechaNacChooser, fechaHace18anios)) {
			return true;
		} else {
			return false;
		}
	}

	private static Date restarAnios(Date fecha, int anios) {
		long tiempoEnMillis = fecha.getTime();
		long unAnioEnMillis = 365 * 24 * 60 * 60 * 1000L;

		return new Date(tiempoEnMillis - (anios * unAnioEnMillis));
	}

	private static boolean esMayorDeEdad(Date fechaNacimiento, Date fechaHace18anios) {
		return fechaNacimiento.before(fechaHace18anios);
	}
}