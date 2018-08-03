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
            String queryString = "from Movimiento as mv where :cuenta is null or mv.cuenta = :cuenta "
                    + "and :detalle is null or :detalle = '' or :detalle = mv.detalle "
                    + "and :montoDesde is null or :montoHasta is null or mv.monto between :montoDesde and :montoHasta "
                    + "and mv.fechaMovimiento between :fechaMovimientoDesde and :fechaMovimientoHasta "
                    + "and :tiposMovimiento is empty or mv.tipoMovimiento in :tiposMovimiento "
                    + "and :categoriasMovimiento is empty or mv.categoriaMovimiento in :categoriasMovimiento "
                    + "order by mv.fechaMovimiento";
            movimientos = session.createQuery(queryString)
                    .setParameter("cuenta", cuenta.getIdCuenta())
                    .setParameter("detalle", detalle)
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
