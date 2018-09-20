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

    @Override
    public void deleteMovimientosByFechaCreacionAndCuenta(Date fechaCreacion, int idCuenta) {
        //Se crea Objeto Session
        Session session; //Se abre una sesion

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        Transaction tx = session.beginTransaction(); //Se inicia una transacción
        try {
            String stringQuery = "DELETE FROM Movimiento m WHERE m.fechaCreacion = :fechaCreacion AND m.cuenta = :cuenta";
            Query query = session.createQuery(stringQuery);
            query.setDate("fechaCreacion", fechaCreacion);
            query.setInteger("cuenta", idCuenta);
            tx.commit();
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
    public double promedio(int[] v) {
        double prom = 0.0;
        for (int i = 0; i < v.length; i++) {
            prom += v[i];
        }

        return prom / (double) v.length;
    }

    @Override
    public double desviacion(int[] v) {
        double prom, sum = 0;
        int i, n = v.length;
        prom = promedio(v);

        for (i = 0; i < n; i++) {
            sum += Math.pow(v[i] - prom, 2);
        }

        return Math.sqrt(sum / (double) n);
    }

    // 0 - Menor a Mayor, 1 - Mayor a menor
    @Override
    public int[] burbuja(int[] v, int ord) {
        int i, j, n = v.length, aux = 0;

        for (i = 0; i < n - 1; i++) {
            for (j = i + 1; j < n; j++) {
                if (ord == 0) {
                    if (v[i] > v[j]) {
                        aux = v[j];
                        v[j] = v[i];
                        v[i] = aux;
                    } else if (ord == 1) {
                        if (v[i] < v[j]) {
                            aux = v[i];
                            v[i] = v[j];
                            v[j] = aux;
                        }
                    }
                }
            }
        }

        return v;
    }

    @Override
    public double mediana(int[] v) {
        int pos = 0, n = v.length;
        double temp = 0, temp0 = 0;
        // ordenar de menor a mayor
        v = burbuja(v, 0);

        temp = n / 2;
        if (n % 2 == 0) {
            pos = (int) temp;
            temp0 = (double) (v[pos] / v[pos + 1]);
        }
        if (n % 2 == 1) {
            pos = (int) (temp + 0.5);
            temp0 = (double) (v[pos]);
        }

        return temp0;
    }

    @Override
    public int rango(int[] v) {
        // ordenar de mayor a menor
        v = burbuja(v, 1);

        return v[v.length - 1] - v[0];
    }

    @Override
    public int moda(int[] v) {
        int i, j, moda = 0, n = v.length, frec;
        int frecTemp, frecModa = 0, moda1 = -1;

        // ordenar de menor a mayor
        v = burbuja(v, 0);

        for (i = 0; i < n; i++) {
            frecTemp = 1;
            for (j = i + 1; j < n; j++) {
                if (v[i] == v[j]) {
                    frecTemp++;
                }
            }
            if (frecTemp > frecModa) {
                frecModa = frecTemp;
                moda1 = v[i];
            }
        }
        return moda1;
    }
}