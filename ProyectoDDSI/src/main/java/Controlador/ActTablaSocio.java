/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Socio;
import Vista.VistaSocios;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JoseAngelRD
 */
public class ActTablaSocio {

	private VistaSocios vSocios;
	private DefaultTableModel modeloTablaSocios;

	public ActTablaSocio(VistaSocios vS) {
		vSocios = vS;
	}

	public void actualizarEditable() {
		modeloTablaSocios = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int colum) {
				return false;
			}
		};
		vSocios.jTable1.setModel(modeloTablaSocios);

	}

	public void dibujarTablaSocios() {
		String[] columnasTabla = { "Codigo", "Nombre", "DNI", "Telefono", "Correo", "Fecha Incorporacion", "Categoria",
				"Fecha Nacimiento" };
		modeloTablaSocios.setColumnIdentifiers(columnasTabla);

		vSocios.jTable1.getTableHeader().setResizingAllowed(false);
		vSocios.jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		vSocios.jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
		vSocios.jTable1.getColumnModel().getColumn(1).setPreferredWidth(240);
		vSocios.jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
		vSocios.jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);
		vSocios.jTable1.getColumnModel().getColumn(4).setPreferredWidth(200);
		vSocios.jTable1.getColumnModel().getColumn(5).setPreferredWidth(150);
		vSocios.jTable1.getColumnModel().getColumn(6).setPreferredWidth(20);
		vSocios.jTable1.getColumnModel().getColumn(7).setPreferredWidth(150);
	}

	public void rellenarTablaSocios(ArrayList<Socio> socios) {
		Object[] fila = new Object[8];
		for (Socio socio : socios) {
			fila[0] = socio.getNumeroSocio();
			fila[1] = socio.getNombre();
			fila[2] = socio.getDni();
			fila[3] = socio.getTelefono();
			fila[4] = socio.getCorreo();
			fila[5] = socio.getFechaEntrada();
			fila[6] = socio.getCategoria();
			fila[7] = socio.getFechaNacimiento();

			modeloTablaSocios.addRow(fila);
		}
	}

	public void vaciarTablaSocios() {
		while (modeloTablaSocios.getRowCount() > 0) {
			modeloTablaSocios.removeRow(0);
		}
	}

	public void rellenarTablaSociosBusqueda(ArrayList<Socio> socios, String tipo, String busqueda) {

		Object[] fila = new Object[8];

		switch (tipo) {
		case "Codigo":
			for (Socio socio : socios) {
				if (socio.getNumeroSocio().contains(busqueda)) {
					fila[0] = socio.getNumeroSocio();
					fila[1] = socio.getNombre();
					fila[2] = socio.getDni();
					fila[3] = socio.getTelefono();
					fila[4] = socio.getCorreo();
					fila[5] = socio.getFechaEntrada();
					fila[6] = socio.getCategoria();
					fila[7] = socio.getFechaNacimiento();

					modeloTablaSocios.addRow(fila);
				}
			}

			break;
		case "Nombre":
			for (Socio socio : socios) {
				if (socio.getNombre().contains(busqueda)) {
					fila[0] = socio.getNumeroSocio();
					fila[1] = socio.getNombre();
					fila[2] = socio.getDni();
					fila[3] = socio.getTelefono();
					fila[4] = socio.getCorreo();
					fila[5] = socio.getFechaEntrada();
					fila[6] = socio.getCategoria();
					fila[7] = socio.getFechaNacimiento();

					modeloTablaSocios.addRow(fila);
				}
			}

			break;
		case "DNI":
			for (Socio socio : socios) {
				if (socio.getDni().contains(busqueda)) {
					fila[0] = socio.getNumeroSocio();
					fila[1] = socio.getNombre();
					fila[2] = socio.getDni();
					fila[3] = socio.getTelefono();
					fila[4] = socio.getCorreo();
					fila[5] = socio.getFechaEntrada();
					fila[6] = socio.getCategoria();
					fila[7] = socio.getFechaNacimiento();

					modeloTablaSocios.addRow(fila);
				}
			}

			break;
		case "Fecha Nacimiento":
			for (Socio socio : socios) {
				if (socio.getFechaNacimiento() != null)
					if (socio.getFechaNacimiento().contains(busqueda)) {
						fila[0] = socio.getNumeroSocio();
						fila[1] = socio.getNombre();
						fila[2] = socio.getDni();
						fila[3] = socio.getTelefono();
						fila[4] = socio.getCorreo();
						fila[5] = socio.getFechaEntrada();
						fila[6] = socio.getCategoria();
						fila[7] = socio.getFechaNacimiento();

						modeloTablaSocios.addRow(fila);
					}
			}

			break;
		case "Telefono":
			for (Socio socio : socios) {
				if (socio.getTelefono() != null)
					if (socio.getTelefono().contains(busqueda)) {
						fila[0] = socio.getNumeroSocio();
						fila[1] = socio.getNombre();
						fila[2] = socio.getDni();
						fila[3] = socio.getTelefono();
						fila[4] = socio.getCorreo();
						fila[5] = socio.getFechaEntrada();
						fila[6] = socio.getCategoria();
						fila[7] = socio.getFechaNacimiento();

						modeloTablaSocios.addRow(fila);
					}
			}

			break;
		case "Correo":
			for (Socio socio : socios) {
				if (socio.getCorreo() != null)
					if (socio.getCorreo().contains(busqueda)) {
						fila[0] = socio.getNumeroSocio();
						fila[1] = socio.getNombre();
						fila[2] = socio.getDni();
						fila[3] = socio.getTelefono();
						fila[4] = socio.getCorreo();
						fila[5] = socio.getFechaEntrada();
						fila[6] = socio.getCategoria();
						fila[7] = socio.getFechaNacimiento();

						modeloTablaSocios.addRow(fila);
					}
			}

			break;
		case "Fecha Entrada":
			for (Socio socio : socios) {
				if (socio.getFechaEntrada().contains(busqueda)) {
					fila[0] = socio.getNumeroSocio();
					fila[1] = socio.getNombre();
					fila[2] = socio.getDni();
					fila[3] = socio.getTelefono();
					fila[4] = socio.getCorreo();
					fila[5] = socio.getFechaEntrada();
					fila[6] = socio.getCategoria();
					fila[7] = socio.getFechaNacimiento();

					modeloTablaSocios.addRow(fila);
				}
			}

			break;
		case "Categoria":
			for (Socio socio : socios) {
				if (socio.getCategoria().equals(busqueda.charAt(0))) {
					fila[0] = socio.getNumeroSocio();
					fila[1] = socio.getNombre();
					fila[2] = socio.getDni();
					fila[3] = socio.getTelefono();
					fila[4] = socio.getCorreo();
					fila[5] = socio.getFechaEntrada();
					fila[6] = socio.getCategoria();
					fila[7] = socio.getFechaNacimiento();

					modeloTablaSocios.addRow(fila);
				}
			}
			break;
		}
	}
}
