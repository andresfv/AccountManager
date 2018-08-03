/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MovimientoService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.CategoriaMovimiento;
import model.Cuenta;
import model.Movimiento;
import model.TipoMovimiento;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class MovimientoServiceImpl implements MovimientoService {

    @Override
    public List<Movimiento> findMovimientosByParametros(Cuenta cuenta, String detalle, double montoDesde, double montoHasta, Date fechaMovimientoDesde, Date fechaMovimientoHasta,
            List<TipoMovimiento> tiposMovimiento, List<CategoriaMovimiento> categoriasMovimiento) {

        List<Movimiento> movimientos = new ArrayList<Movimiento>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy/MM/dd");

        //Se crea Objeto Session
        Session session; //Se abre una sesion
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction(); //Se inicia una transacción       
        try {
            String queryString = "from Movimiento as mv where mv.cuenta = :cuenta "
                    + "and mv.monto between :montoDesde and :montoHasta "
                    + "and mv.fechaMovimiento between :fechaMovimientoDesde and :fechaMovimientoHasta "
                    + "and mv.tipoMovimiento.idTipoMovimiento in :tiposMovimiento "
                    + "and mv.categoriaMovimiento.idCategoriaMovimiento in :categoriasMovimiento "
                    + "order by mv.fechaMovimiento";
            
            if (!detalle.equals("")) {
                queryString += "and mv.detalle like :detalle ";
            }
            
            movimientos = session.createQuery(queryString)
                    .setParameter("cuenta", cuenta.getIdCuenta())
                    .setParameter("detalle", "%" + detalle + "%")
                    .setParameter("montoDesde", montoDesde)
                    .setParameter("montoHasta", montoHasta)
                    .setParameter("fechaMovimientoDesde", simpleDateFormat.parse(simpleDateFormat.format(fechaMovimientoDesde)))
                    .setParameter("fechaMovimientoHasta", simpleDateFormat.parse(simpleDateFormat.format(fechaMovimientoHasta)))
                    .setParameterList("tiposMovimiento", tiposMovimiento)
                    .setParameterList("categoriasMovimiento", categoriasMovimiento).list();

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