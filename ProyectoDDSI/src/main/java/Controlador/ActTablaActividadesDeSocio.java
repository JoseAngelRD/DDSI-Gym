/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Vista.VistaSocios;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Vista.VistaInsertarBajarActividad;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;

/**
 *
 * @author JoseAngelRD
 */
public class ActTablaActividadesDeSocio {

	private VistaInsertarBajarActividad vAltBajSocio;
	private DefaultTableModel modeloTablaActSocios;

	public ActTablaActividadesDeSocio(VistaInsertarBajarActividad vS) {
		vAltBajSocio = vS;
	}

	public void actualizarEditable() {
		modeloTablaActSocios = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int colum) {
				return false;
			}
		};
		vAltBajSocio.jTable1.setModel(modeloTablaActSocios);

	}

	public void dibujarTablaActSocios() {

		String[] columnasTabla = { "ID", "Nombre" };
		modeloTablaActSocios.setColumnIdentifiers(columnasTabla);

		vAltBajSocio.jTable1.getTableHeader().setResizingAllowed(false);
		vAltBajSocio.jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		vAltBajSocio.jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
		vAltBajSocio.jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);

	}

	public void rellenarTablaActSocios(Set<Actividad> actividades, Session sesion) throws Exception {

		vAltBajSocio.jComboBoxBaja.removeAllItems();
		vAltBajSocio.jComboBoxIns.removeAllItems();

		ArrayList<Actividad> todasActs = ActividadDAO.listaActividadesConHQL(sesion);
		ArrayList<Actividad> actsDentro = new ArrayList<>();

		Object[] fila = new Object[2];
		for (Actividad act : actividades) {
			vAltBajSocio.jComboBoxBaja.addItem(act.getNombre());
			actsDentro.add(act);
			fila[0] = act.getIdActividad();
			fila[1] = act.getNombre();

			modeloTablaActSocios.addRow(fila);
		}
		boolean esta = false;
		for (int i = 0; i < todasActs.size(); i++) {
			for (int j = 0; j < actsDentro.size(); j++) {
				if (todasActs.get(i).getIdActividad().equals(actsDentro.get(j).getIdActividad()))
					esta = true;
			}
			if (!esta) {
				vAltBajSocio.jComboBoxIns.addItem(todasActs.get(i).getNombre());
			}
			esta = false;
		}
	}

	public void vaciarTablaSocios() {
		while (modeloTablaActSocios.getRowCount() > 0) {
			modeloTablaActSocios.removeRow(0);
		}
	}
}
