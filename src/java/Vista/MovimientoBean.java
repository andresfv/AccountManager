/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.HibernateService;
import Modelo.CategoriaMovimiento;
import Modelo.Cuenta;
import Modelo.Movimiento;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class MovimientoBean {

    HibernateService hibernateService;
    Cuenta cuenta;
    Movimiento movimiento;

    List<Movimiento> listaMovimientos;
    List<Movimiento> listaMovimientosFiltrados;

    public MovimientoBean() {
        hibernateService = new HibernateService();
        cuenta = new Cuenta();
        movimiento = new Movimiento();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        cargaListaMovimientos();
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<Movimiento> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public List<Movimiento> getListaMovimientosFiltrados() {
        return listaMovimientosFiltrados;
    }

    public void setListaMovimientosFiltrados(List<Movimiento> listaMovimientosFiltrados) {
        this.listaMovimientosFiltrados = listaMovimientosFiltrados;
    }

    public List<Movimiento> cargaListaMovimientos() {
        listaMovimientos.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAllByEqual("Movimiento", "cuenta", cuenta.toString()));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaMovimientos.add((Movimiento) objeto);
            }
        }
        listaMovimientosFiltrados = listaMovimientos;
        return listaMovimientos;
    }

    public void onRowEdit(RowEditEvent event) {
        this.movimiento = ((Movimiento) event.getObject());
        onSave();
        cargaListaMovimientos();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.movimiento = ((Movimiento) object);
        deleteMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaMovimientos();
        return "";
    }

    public String onSave() {
        saveMovimiento(this.movimiento);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaMovimientos();
        return "cuentaEditForm";
    }

    public String newMovimiento(Cuenta cuenta) {
        this.movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        return "movimientoEditForm";
    }

    public void saveMovimiento(Movimiento movimiento) {
        try {
            hibernateService.save(movimiento);
        } catch (Exception e) {
            System.out.println("Error "+e);
        }

    }

    public void deleteMovimiento() {
        try {
            hibernateService.delete(this.movimiento);
        } catch (Exception e) {
        }
    }
}
