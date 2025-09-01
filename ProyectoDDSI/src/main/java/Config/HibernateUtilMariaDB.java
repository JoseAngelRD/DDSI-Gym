package Config;

import javax.imageio.spi.ServiceRegistry;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author usuario
 */
public class HibernateUtilMariaDB {

	private static SessionFactory sessionFactory;

	private static SessionFactory buildSessionFactory() {
		try {
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.configure("hibernateMariaDB.cfg.xml").build();

			Metadata metadata = new MetadataSources((org.hibernate.service.ServiceRegistry) serviceRegistry)
					.getMetadataBuilder().build();

			return metadata.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			System.err.println("Build SeesionFactory MariaDB failed :" + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

	public static void close() {
		if ((sessionFactory != null) && (sessionFactory.isClosed() == false)) {
			sessionFactory.close();
		}
	}
}
