/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.HibernateService;
import Modelo.CategoriaMovimiento;
import Modelo.Movimiento;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class MovimientoBean {

    HibernateService hibernateService;
    Movimiento movimiento;
    CategoriaMovimientoBean categoriaMovimientoBean;

    public MovimientoBean() {
        hibernateService = new HibernateService();
        movimiento = new Movimiento();
        categoriaMovimientoBean = new CategoriaMovimientoBean();
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<CategoriaMovimiento> completoCategoriaMovimiento(String nombre) {
        return categoriaMovimientoBean.completoCategoriasMovimiento(nombre);
    }
}
