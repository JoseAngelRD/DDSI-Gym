/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author usuario
 */
public class MonitorDAO {

	public static ArrayList<Monitor> listaMonitoresConHQL(Session sesion) throws Exception {

		Query consulta = sesion.createQuery("SELECT m FROM Monitor m", Monitor.class);
		ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list();
		return monitores;
	}

	public static ArrayList<Object[]> listaNombreDNIMonitores(Session sesion) throws Exception {

		Query consulta = sesion.createQuery("SELECT m.nombre, m.dni FROM Monitor m");
		ArrayList<Object[]> monitores = (ArrayList<Object[]>) consulta.list();
		return monitores;
	}

	public static void insertarActualizarMonitor(Session sesion, Monitor monitor) throws Exception {
		sesion.saveOrUpdate(monitor);
	}

	public static void bajaMonitor(Session sesion, String codMonitor) throws Exception {
		Query consulta = sesion.createNativeQuery("SELECT * FROM MONITOR WHERE codMonitor=:mon", Monitor.class);
		consulta.setParameter("mon", codMonitor);
		sesion.delete((Monitor) consulta.getSingleResult());

	}

	public static ArrayList<Monitor> monitorResponsableActividad(Session session, String idActividad) throws Exception {

		// Selecciono el monitor donde la id de actividades de las que es responsable
		// corresponde con la idAct pasada por parametro y devuelvo sus campos
		Query consulta = session.createQuery(
				"SELECT m FROM Monitor m JOIN m.actividadesResponsable a WHERE a.idActividad = :idActividad",
				Monitor.class);
		consulta.setParameter("idActividad", idActividad);
		ArrayList<Monitor> monitorResponsable = (ArrayList<Monitor>) consulta.list();
		return monitorResponsable;
	}

}
