package Modelo;

import Vista.VistaMensajes;
import java.sql.*;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;

public class SocioDAO {

	//
	public static ArrayList<Socio> listaSociosHQL(Session sesion) throws Exception {

		Query consulta = sesion.createQuery("SELECT s FROM Socio s", Socio.class); // creamos la consulta HQL, obtenemos
																					// todos los socios con todos sus
																					// parametros
		ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
		return socios; // devolvemos una arraylist con los socios
	}

	public static ArrayList<Socio> listaSociosSQL(Session sesion) throws Exception {
		Query consulta = sesion.createNativeQuery("SELECT * FROM Socio", Socio.class); // creamos la consulta SQL,
																						// obtenemos todos los socios
																						// con todos sus parametros
		ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

		return socios; // devolvemos una arraylist con los socios
	}

	public static ArrayList<Socio> listaSociosConsultaNombrada(Session sesion) throws Exception {
		Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class); // creamos la consulta nombrada,
																				// obtenemos todos los socios con todos
																				// sus parametros
		ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

		return socios; // devolvemos una arraylist con los socios
	}

	public static ArrayList<Object[]> listaNombreCategoriaSocios(Session sesion, String categoria) throws Exception {

		// selecciono nombre y la categoria pasasda por parametro en una consulta, lo
		// guardo en un Object[] y lo devuelvo

		Query consulta = sesion.createQuery("SELECT s.categoria, s.nombre FROM Socio s WHERE s.categoria = :categoria");
		consulta.setParameter("categoria", categoria);
		ArrayList<Object[]> nombreYCategoria = (ArrayList<Object[]>) consulta.list();
		return nombreYCategoria;
	}

	public static ArrayList<Object[]> listaNombreTelefono(Session sesion) throws Exception {

		// selecciono nombre y telefono en una consulta, lo guardo en un Object[] y lo
		// devuelvo
		Query consulta = sesion.createQuery("SELECT s.nombre, s.telefono FROM Socio s");
		ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.list();
		return socios;
	}

	public static ArrayList<Socio> sociosActividad(Session session, String idActividad) throws Exception {

		// selecciono todo sobre una actividad pasasda por parametro en una consulta, lo
		// guardo en un ArrayList<Socio> y lo devuelvo

		Query consulta = session.createQuery(
				"SELECT s FROM Socio s JOIN s.actividades a WHERE a.idActividad = :idActividad", Socio.class);
		consulta.setParameter("idActividad", idActividad);
		ArrayList<Socio> sociosPorActividad = (ArrayList<Socio>) consulta.list();

		return sociosPorActividad;
	}

	public static void insertarActualizarSocio(Session sesion, Socio socio) throws Exception {
		sesion.saveOrUpdate(socio); // inserto un socio completo pasado por parametro
	}

	// doy de baja a un socio pasado por parametro
	public static void bajaSocio(Session sesion, String numerosocio) throws Exception {
		Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO WHERE numerosocio=:mon", Socio.class);
		consulta.setParameter("mon", numerosocio);
		sesion.delete((Socio) consulta.getSingleResult());

	}

	public static void actualizaCategoria(Session session, String idSocio, Character nuevaCategoria) throws Exception {

		// actualizo la categoria de un socio pasada por parametro

		Query consulta = session.createQuery("SELECT s FROM Socio s WHERE s.numeroSocio = :idSocio", Socio.class);
		consulta.setParameter("idSocio", idSocio);
		Socio socioModificar = (Socio) consulta.uniqueResult();
		socioModificar.setCategoria(nuevaCategoria);
		session.saveOrUpdate(socioModificar);

	}

	public static void inscribirActividad(Session session, String idSocio, String nombre) throws Exception {

		// inscribo en una actividad a un socio pasado por parametro

		Query consulta = session.createQuery("SELECT s FROM Socio s WHERE s.numeroSocio = :idSocio", Socio.class);
		consulta.setParameter("idSocio", idSocio);
		Socio socioInscribir = (Socio) consulta.uniqueResult();

		Query consultaAct = session.createQuery("SELECT a FROM Actividad a WHERE a.nombre = :nombre", Actividad.class);
		consultaAct.setParameter("nombre", nombre);
		Actividad actividadInscribir = (Actividad) consultaAct.uniqueResult();

		// inscribo el socio en una actividad y a la actividad le inscribo un socio

		socioInscribir.getActividades().add(actividadInscribir);
		session.saveOrUpdate(socioInscribir);

		actividadInscribir.getSocios().add(socioInscribir);
		session.saveOrUpdate(actividadInscribir);
	}

	public static void bajaActividad(Session session, String idSocio, String nombre) throws Exception {

		Query consulta = session.createQuery("SELECT s FROM Socio s WHERE s.numeroSocio = :idSocio", Socio.class);
		consulta.setParameter("idSocio", idSocio);
		Socio socioBaja = (Socio) consulta.uniqueResult();

		Query consultaAct = session.createQuery("SELECT a FROM Actividad a WHERE a.nombre = :nombre", Actividad.class);
		consultaAct.setParameter("nombre", nombre);
		Actividad actividadBaja = (Actividad) consultaAct.uniqueResult();

		// bajo el socio en una actividad y a la actividad le bajo un socio

		socioBaja.getActividades().remove(actividadBaja);
		session.saveOrUpdate(socioBaja);

		actividadBaja.getSocios().remove(socioBaja);
		session.saveOrUpdate(actividadBaja);
	}

}
