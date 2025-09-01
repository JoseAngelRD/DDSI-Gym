package Vista;

import Modelo.Actividad;
import Modelo.Monitor;
import Modelo.Socio;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.hibernate.Session;

public class VistaMensajes {

	public void Mensaje(Component C, String tipoMensaje, String texto) {
		switch (tipoMensaje) {
		case "info":
			JOptionPane.showMessageDialog(C, texto, "Información", JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}

	public void mensajeConsola(String texto) {
		System.out.println(texto);
		JOptionPane.showMessageDialog(null, "Conexion correcta con Hibernate", "Conexion OK",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void mensajeErrorNombre(Component padre) {
		JOptionPane.showMessageDialog(padre, "El nombre no puede ser nulo", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorDNI(Component padre) {
		JOptionPane.showMessageDialog(padre, "El DNI tendra el siguiente formato: XXXXXXXXXL", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorCorreo(Component padre) {
		JOptionPane.showMessageDialog(padre, "El correo tendra el siguiente formato: XXX@XXX", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorTelefono(Component padre) {
		JOptionPane.showMessageDialog(padre, "El telefono tendra 9 digitos", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorNick(Component padre) {
		JOptionPane.showMessageDialog(padre, "El nick no puede tener mas de 6 caracteres", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorCategoria(Component padre) {
		JOptionPane.showMessageDialog(padre, "La categoria no puede tener mas de 1 caracter o ser nula", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeErrorFecha(Component padre) {
		JOptionPane.showMessageDialog(padre, "Error en la fecha insertada", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mensajeConsola(String texto, String error) {
		System.out.println(texto);
		System.out.println("Error: " + error);
		JOptionPane.showMessageDialog(null, "Error en la conexion con Hibernate\nError: " + error, "Conexion Fallida",
				JOptionPane.ERROR_MESSAGE);

	}

	public void mensajeSocioTelefono(ArrayList<Object[]> lsocio) {
		int cat = lsocio.size();
		System.out.println("LISTA: ");

		for (int i = 0; i < cat; i++) {
			System.out.println(lsocio.get(i)[0] + ", " + lsocio.get(i)[1]);
		}

	}

	public static void muestraSocios(ArrayList<Socio> lSocios) {

		int nSocios = lSocios.size();
		System.out.println("LISTA DE SOCIOS: ");

		for (int i = 0; i < nSocios; i++) {
			System.out.println(
					lSocios.get(i).getNumeroSocio() + ", " + lSocios.get(i).getDni() + ", " + lSocios.get(i).getNombre()
							+ ", " + lSocios.get(i).getCorreo() + ", " + lSocios.get(i).getTelefono() + ", "
							+ lSocios.get(i).getFechaEntrada() + ", " + lSocios.get(i).getFechaNacimiento());
		}
	}

	public void muestraActividades(ArrayList<Actividad> lActividades) {

		int nSocios = lActividades.size();
		System.out.println("LISTA DE ACTIVIDADES: ");

		for (int i = 0; i < nSocios; i++) {
			System.out.println(lActividades.get(i).getNombre());
		}
	}

	public void muestraMonitores(ArrayList<Monitor> lMonitores) {

		int nSocios = lMonitores.size();
		System.out.println("LISTA DE MONITORES: ");

		for (int i = 0; i < nSocios; i++) {
			System.out.println(lMonitores.get(i).getCodMonitor() + ", " + lMonitores.get(i).getNombre() + ", "
					+ lMonitores.get(i).getDni() + ", " + lMonitores.get(i).getCorreo() + ", "
					+ lMonitores.get(i).getTelefono() + ", " + lMonitores.get(i).getFechaEntrada() + ", "
					+ lMonitores.get(i).getNick());
		}
	}

	public int muestraEliminarPersona(Component padre, String nom) {
		return JOptionPane.showConfirmDialog(padre, "Desea eliminar a " + nom, "Eliminar miembro",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

	}

}
