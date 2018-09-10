/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.HibernateService;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
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
        Session session;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            session.saveOrUpdate(object); //Se almacena el objeto deseado
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    @Override
    public void delete(Object object) {
        //Se crea Objeto Session
        Session session; //Se abre una sesion

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            session.delete(object); //Se almacena el objeto deseado
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Object> findAll(String objectName) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " order by id" + objectName;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public Object findById(int idObject, String objectName) {
        Object object = new Object();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " where id" + objectName + " = " + idObject;
            Query query = session.createQuery(queryString);
            object = query.uniqueResult();
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return object;

    }

    @Override
    public List<Object> findAllByEqual(String objectName, String column, String value) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " as obj where obj." + column + " = " + value;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public List<Object> findAllByEqual(String objectName, String column, Integer value) {
        List<Object> objects = new ArrayList<Object>();
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from " + objectName + " as obj where obj." + column + " = " + value;
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public List<Object> findAllByLike(String objectName, String column, String value) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
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
            if (session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public List<Object> runQuery(String queryString) {
        List<Object> objects = new ArrayList<Object>();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción       
        try {
            Query query = session.createQuery(queryString);
            objects.addAll(query.list());
            tx.commit(); //Se comitea en la base de datos
            //session.close(); //Se cierra la sesion
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public Connection getConection() {
        Session session;
        Connection conn = null;

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        Transaction tx = session.beginTransaction(); //Se inicia una transacción       

        try {
            conn = session.doReturningWork((Connection conn1) -> conn1);
            tx.commit();
            // do your work using connection
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

//        try {
//            SessionImpl sessionImpl = (SessionImpl) session;
//            conn = sessionImpl.connection();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            if (session.isOpen()) {
//                session.close();
//            }
//        }
        return conn;
    }
}
