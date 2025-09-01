/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Vista.VistaMonitores;
import Vista.VistaSocios;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JoseAngelRD
 */
public class ActTablaMonitor {

	private VistaMonitores vMonitor;
	private DefaultTableModel modeloTablaMonitores;

	public ActTablaMonitor(VistaMonitores vM) {
		vMonitor = vM;
	}

	public void actualizarEditable() {
		modeloTablaMonitores = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int colum) {
				return false;
			}
		};
		vMonitor.jTable1.setModel(modeloTablaMonitores);

	}

	public void dibujarTablaMonitores() {
		String[] columnasTabla = { "Codigo", "Nombre", "DNI", "Telefono", "Correo", "Fecha Incorporacion", "Nick" };
		modeloTablaMonitores.setColumnIdentifiers(columnasTabla);

		vMonitor.jTable1.getTableHeader().setResizingAllowed(false);
		vMonitor.jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		vMonitor.jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
		vMonitor.jTable1.getColumnModel().getColumn(1).setPreferredWidth(240);
		vMonitor.jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
		vMonitor.jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);
		vMonitor.jTable1.getColumnModel().getColumn(4).setPreferredWidth(200);
		vMonitor.jTable1.getColumnModel().getColumn(5).setPreferredWidth(150);
		vMonitor.jTable1.getColumnModel().getColumn(6).setPreferredWidth(60);
	}

	public void rellenarTablaMonitores(ArrayList<Monitor> monitores) {
		Object[] fila = new Object[7];
		for (Monitor monitor : monitores) {
			fila[0] = monitor.getCodMonitor();
			fila[1] = monitor.getNombre();
			fila[2] = monitor.getDni();
			fila[3] = monitor.getTelefono();
			fila[4] = monitor.getCorreo();
			fila[5] = monitor.getFechaEntrada();
			fila[6] = monitor.getNick(); // nick

			modeloTablaMonitores.addRow(fila);
		}

	}

	public void vaciarTablaMonitores() {
		while (modeloTablaMonitores.getRowCount() > 0) {
			modeloTablaMonitores.removeRow(0);
		}
	}

}
