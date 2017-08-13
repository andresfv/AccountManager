/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Hibernate.HibernateUtil;
import Modelo.CategoriaMovimiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class EntityService {

    public void saveObjeto(CategoriaMovimiento categoriaMovimiento) {

        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        //SessionFactory sessionFactory = HibernateUtil.getSessionFactory();//Se crea un sessionFactory
        //Se crea Objeto Session
        Session session = sessionFactory.openSession(); //Se abre una sesion
        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        session.saveOrUpdate(categoriaMovimiento); //Se almacena el objeto deseado
        tx.commit(); //Se comitea en la base de datos
        session.close(); //Se cierra la sesion
    }
}
