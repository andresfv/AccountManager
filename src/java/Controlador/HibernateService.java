/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Hibernate.HibernateUtil;
import Modelo.CategoriaMovimiento;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class HibernateService {

    public void save(Object object) {

        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        session.saveOrUpdate(object); //Se almacena el objeto deseado
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
    }

    public void delete(Object object) {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        session.delete(object); //Se almacena el objeto deseado
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
    }

    public List<Object> findAll(String objectName) {
        List<Object> objects = new ArrayList<Object>();
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        String queryString = "from " + objectName + " order by id" + objectName;
        System.out.println("OJO "+queryString);
        Query query = session.createQuery(queryString);
        objects.addAll(query.list());
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
        return objects;
    }

    public Object findById(int idObject, String objectName) {
        Object object;
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        String queryString = "from " + objectName + " where id" + objectName + " = " + idObject;
        Query query = session.createQuery(queryString);
        object = query.uniqueResult();
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
        return object;
    }

    public List<Object> findAllBy(String queryString) {
        List<Object> objects = new ArrayList<Object>();
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        Query query = session.createQuery(queryString);
        objects.addAll(query.list());
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
        return objects;
    }
}
