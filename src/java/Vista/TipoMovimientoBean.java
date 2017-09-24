/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.HibernateService;
import Modelo.CategoriaMovimiento;
import Modelo.TipoMovimiento;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.transaction.TransactionScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class TipoMovimientoBean {

    HibernateService hibernateService;
    TipoMovimiento tipoMovimiento;
    List<TipoMovimiento> listaTiposMovimiento;
    List<TipoMovimiento> listaTiposMovimientoFiltradas;

    public TipoMovimientoBean() {
        hibernateService = new HibernateService();
        tipoMovimiento = new TipoMovimiento();
        listaTiposMovimiento = new ArrayList<TipoMovimiento>();
        listaTiposMovimientoFiltradas = new ArrayList<TipoMovimiento>();
        cargaListaTiposMovimiento();
    }

    public List<TipoMovimiento> getListaTiposMovimiento() {
        return listaTiposMovimiento;
    }

    public void setListaTiposMovimiento(List<TipoMovimiento> listaTiposMovimiento) {
        this.listaTiposMovimiento = listaTiposMovimiento;
    }

    public List<TipoMovimiento> getListaTiposMovimientoFiltradas() {
        return listaTiposMovimientoFiltradas;
    }

    public void setListaTiposMovimientoFiltradas(List<TipoMovimiento> listaTiposMovimientoFiltradas) {
        this.listaTiposMovimientoFiltradas = listaTiposMovimientoFiltradas;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public List<TipoMovimiento> cargaListaTiposMovimiento() {
        listaTiposMovimiento.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("TipoMovimiento"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaTiposMovimiento.add((TipoMovimiento) objeto);
            }
        }
        listaTiposMovimientoFiltradas = listaTiposMovimiento;
        return listaTiposMovimiento;
    }
    
        public List<TipoMovimiento >completoTipoMovimiento() {
        List<Object> listaObjetos = new ArrayList<Object>();
        List<TipoMovimiento> tiposMovimiento = new ArrayList<TipoMovimiento>();
        listaObjetos.addAll(hibernateService.findAll("TipoMovimiento"));

        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                tiposMovimiento.add((TipoMovimiento) objeto);
            }
        }
        return tiposMovimiento;
    }

    public void onRowEdit(RowEditEvent event) {
        this.tipoMovimiento = ((TipoMovimiento) event.getObject());
        onSave();
        cargaListaTiposMovimiento();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.tipoMovimiento = ((TipoMovimiento) object);
        deleteTipoMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaTiposMovimiento();
        return "";
    }

    public String onSave() {
        saveTipoMovimiento(this.tipoMovimiento);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaTiposMovimiento();
        return "";
    }

    public void newTipoMovimiento() {
        this.tipoMovimiento = new TipoMovimiento();
    }

    public void saveTipoMovimiento(TipoMovimiento tipoMovimientoObj) {
        try {
            hibernateService.save(tipoMovimientoObj);
        } catch (Exception e) {
        }

    }

    public void deleteTipoMovimiento() {
        try {
            hibernateService.delete(this.tipoMovimiento);
        } catch (Exception e) {
        }
    }
}
