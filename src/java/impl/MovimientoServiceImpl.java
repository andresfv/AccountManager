/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MovimientoService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CategoriaMovimiento;
import model.Cuenta;
import model.Movimiento;
import model.TipoMovimiento;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class MovimientoServiceImpl implements MovimientoService {

    @Override
    public List<Movimiento> findMovimientosByParametros(Cuenta cuenta, String detalle, Date fechaMovimientoDesde, Date fechaMovimientoHasta,
            List<TipoMovimiento> tiposMovimiento, List<CategoriaMovimiento> categoriasMovimiento) {

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Collection> paramsList = new HashMap<String, Collection>();
        List<Movimiento> movimientos = new ArrayList<Movimiento>();

        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción       
        try {

            params.put("cuenta", cuenta.getIdCuenta());
            String queryString = "from Movimiento as mv where mv.cuenta = coalesce(:cuenta, mv.cuenta) ";

            if (detalle != null && !detalle.equals("")) {
                queryString += "and mv.detalle like :detalle ";
                params.put("detalle", '%' + detalle + '%');
            }

            if (fechaMovimientoDesde != null && fechaMovimientoHasta != null) {
                queryString += "and mv.fechaMovimiento between :fechaMovimientoDesde and :fechaMovimientoHasta ";
                params.put("fechaMovimientoDesde", fechaMovimientoDesde);
                params.put("fechaMovimientoHasta", fechaMovimientoHasta);
            }

            if (tiposMovimiento != null && tiposMovimiento.size() > 0) {
                queryString += "and Cast(mv.tipoMovimiento.idTipoMovimiento as string) in :tiposMovimiento ";
                paramsList.put("tiposMovimiento", tiposMovimiento);
            }

            if (categoriasMovimiento != null && categoriasMovimiento.size() > 0) {
                queryString += "and Cast(mv.categoriaMovimiento.idCategoriaMovimiento as string) in :categoriasMovimiento ";
                paramsList.put("categoriasMovimiento", categoriasMovimiento);
            }

            queryString += "order by mv.fechaMovimiento";

            Query query = session.createQuery(queryString);

            for (String nombreParametro : query.getNamedParameters()) {
                if (nombreParametro.equals("tiposMovimiento")
                        || nombreParametro.equals("categoriasMovimiento")) {
                    query.setParameterList(nombreParametro, paramsList.get(nombreParametro));
                } else {
                    query.setParameter(nombreParametro, params.get(nombreParametro));
                }
            }
           // System.out.println("QUERY: " + query.getQueryString());
            movimientos = query.list();

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
        return movimientos;
    }
}
//BASARSE EN
//
//Session session = sessionFactory.getCurrentSession();
//        List<Plot> searchedLists = new ArrayList<Plot>();
//        Map<String, Object> params = new HashMap<String,Object>();
//        String hqlQuery = "from Plot where societyBlock.societyBlockId = :societyBlock";
//        params.put( "societyBlock", societyId );
//        if(plotType != null)
//        {
//            hqlQuery += " and type.typeId = :type";
//            params.put( "type", plotType );
//        }
//        if(!plotSize.isEmpty() && plotSize != null && !plotSize.equals( "" ))
//        {
//            hqlQuery += " and size = :size";
//            params.put( "size", plotSize );
//        }
//        if(min != null)
//        {
//            hqlQuery += " and price >= :pricemin";
//            params.put( "pricemin", min );
//        }
//        if(max != null)
//        {
//            hqlQuery += " and price <= :pricemax";
//            params.put( "pricemax", max );
//        }
//        Query query = session.createQuery( hqlQuery );
//
//        for (String str : query.getNamedParameters())
//        {
//            query.setParameter( str, params.get( str ) );
//        }
//        searchedLists = (List<Plot>) query.list();
//        System.out.println( searchedLists.size() );
//        return searchedLists;
