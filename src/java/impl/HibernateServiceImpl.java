/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.HibernateService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class HibernateServiceImpl implements HibernateService {

    @Override
    public void save(Object object) {
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            session.saveOrUpdate(object); //Se almacena el objeto deseado
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }

    }

    @Override
    public void delete(Object object) {
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            session.delete(object); //Se almacena el objeto deseado
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion}
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
    }

    @Override
    public List<Object> findAll(String objectName) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " order by id" + objectName;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
        return objects;
    }

    @Override
    public Object findById(int idObject, String objectName) {
        Object object = new Object();
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " where id" + objectName + " = " + idObject;
            Query query = session.createQuery(queryString);
            object = query.uniqueResult();
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
        return object;

    }

    @Override
    public List<Object> findAllByEqual(String objectName, String column, String value) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " as obj where obj." + column + " = " + value;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
        return objects;
    }
    
    @Override
    public List<Object> findAllByEqual(String objectName, String column, Object value) {
        List<Object> objects = new ArrayList<Object>();
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " as obj where obj." + column + " = " + value;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
        return objects;
    }

    @Override
    public List<Object> findAllByLike(String objectName, String column,
            String value
    ) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " where " + column + " like '%" + value + "%'";
            System.out.println("OJO " + queryString);
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            session.close(); //Se cierra la sesion
            return objects;
        }

    }

    @Override
    public List<Object> runQuery(String queryString
    ) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session = HibernateUtil.getSessionFactory().openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción       
        try {
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
            session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }
        return objects;
    }
}
