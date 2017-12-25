/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public interface HibernateService {

    public void save(Object object);

    public void delete(Object object);

    public List<Object> findAll(String objectName);

    public Object findById(int idObject, String objectName);

    public List<Object> findAllByEqual(String objectName, String column, String value);
    
    public List<Object> findAllByEqual(String objectName, String column, Integer value);

    public List<Object> findAllByLike(String objectName, String column, String value);

    public List<Object> runQuery(String queryString);
}
