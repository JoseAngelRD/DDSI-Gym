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
public class ActividadDAO {

	public static ArrayList<Actividad> listaActividadesConHQL(Session sesion) throws Exception {

		Query consulta = sesion.createQuery("SELECT a FROM Actividad a", Actividad.class);
		ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
		return actividades;
	}

	public static ArrayList<Actividad> ResponsableActividad(Session sesion) throws Exception {

		String act = "XD";
		System.out.println("Inserte la actividad para obtener el responsable");

		Query consulta = sesion.createQuery("SELECT * FROM m.Monitor where m.actividad = " + act);
		ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
		return actividades;
	}

}
