/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;
import model.Cuenta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import model.Movimiento;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CuentaBean {

    HibernateService hibernateService;
    Cuenta cuenta;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    List<Movimiento> listaMovimientos;
    List<Movimiento> listaMovimientosFiltrados;
    MovimientoBean movimientoBean;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        movimientoBean = new MovimientoBean();
        movimientoBean.init();
    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaCuentas();
    }

    public void initDetails(ComponentSystemEvent event) {
        cargaMovimientosCuenta();
    }

    public void cargaMovimientosCuenta() {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimientoBean.cargaListaMovimientos(cuenta.getIdCuenta()));
        listaMovimientosFiltrados = this.listaMovimientos;
    }

    public List<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<Cuenta> getListaCuentasFiltradas() {
        return listaCuentasFiltradas;
    }

    public void setListaCuentasFiltradas(List<Cuenta> listaCuentasFiltradas) {
        this.listaCuentasFiltradas = listaCuentasFiltradas;
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

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public List<Cuenta> cargaListaCuentas() {
        listaCuentas.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("Cuenta"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaCuentas.add((Cuenta) objeto);
            }
        }
        listaCuentasFiltradas = listaCuentas;
        return listaCuentas;
    }

    public void onRowEdit(RowEditEvent event) {
        this.cuenta = ((Cuenta) event.getObject());
        onSave();
        cargaListaCuentas();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.cuenta = ((Cuenta) object);
        deleteCuenta();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaCuentas();
        return "";
    }

    public String onSave() {
        saveCuenta(this.cuenta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaCuentas();
        return "cuentaListForm";
    }

    public String newCuenta() {
        this.cuenta = new Cuenta();
        return "cuentaEditForm";
    }

    public String editCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        return "cuentaEditForm";
    }

    public void saveCuenta(Cuenta cuentaObj) {
        try {
            hibernateService.save(cuentaObj);
        } catch (Exception e) {
        }

    }

    public void deleteCuenta() {
        try {
            hibernateService.delete(this.cuenta);
        } catch (Exception e) {
        }
    }
}
