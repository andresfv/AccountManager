/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ParametroService;
import model.Parametro;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class ParametroServiceImpl implements ParametroService {

    @Override
    public Parametro findByLlave(String llave) {
        Parametro parametro = new Parametro();
        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String queryString = "from Parametro p where p.llave = '" + llave+"'";
            Query query = session.createQuery(queryString);
            parametro = (Parametro) query.uniqueResult();
            tx.commit(); //Se comitea en la base de datos
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return parametro;
    }
}
