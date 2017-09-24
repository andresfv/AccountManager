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
import Modelo.TipoMovimiento;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.metamodel.source.annotations.xml.mocker.MockHelper;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CuentaBean extends MovimientoBean{

    HibernateService hibernateService;
    Cuenta cuenta;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    CategoriaMovimientoBean categoriaMovimientoBean;
    TipoMovimientoBean tipoMovimientoBean;
    List<CategoriaMovimiento> categoriasMovimiento = new ArrayList<CategoriaMovimiento>();
    List<TipoMovimiento> tiposMovimiento = new ArrayList<TipoMovimiento>();

    public CuentaBean() {
        hibernateService = new HibernateService();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        categoriaMovimientoBean = new CategoriaMovimientoBean();
        tipoMovimientoBean = new TipoMovimientoBean();
        cargaListaCuentas();
        cargaCategoriasMovimiento();
        cargaTiposMovimiento();
        cargaListaMovimientos();
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

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public List<CategoriaMovimiento> getCategoriasMovimiento() {
        return categoriasMovimiento;
    }

    public List<TipoMovimiento> getTiposMovimiento() {
        return tiposMovimiento;
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

    public void cargaCategoriasMovimiento() {
        categoriasMovimiento.clear();
        categoriasMovimiento.addAll(categoriaMovimientoBean.completoCategoriasMovimiento());
    }
    
    public void cargaTiposMovimiento(){
       tiposMovimiento.clear();
       tiposMovimiento.addAll(tipoMovimientoBean.completoTipoMovimiento());
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
